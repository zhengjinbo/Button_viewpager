package com.testcar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.app.Activity;

/**
 * activity堆栈管理
 * 
 * @author blue
 */
public class ActivityStack
{
	private static ActivityStack mSingleInstance;
	private Stack<Activity> mActicityStack;

	private ActivityStack()
	{
		mActicityStack = new Stack<Activity>();
	}

	public static ActivityStack getInstance()
	{
		if (null == mSingleInstance)
		{
			mSingleInstance = new ActivityStack();
		}
		return mSingleInstance;
	}

	public Stack<Activity> getStack()
	{
		return mActicityStack;
	}

	/**
	 * 入栈
	 * 
	 * @author blue
	 */
	public void addActivity(Activity activity)
	{
		mActicityStack.push(activity);
	}

	/**
	 * 出栈
	 * 
	 * @author blue
	 */
	public void removeActivity(Activity activity)
	{
		mActicityStack.remove(activity);
	}

	/**
	 * 彻底退出
	 * 
	 * @author blue
	 */
	public void finishAllActivity()
	{
		Activity activity;
		while (!mActicityStack.empty())
		{
			activity = mActicityStack.pop();
			if (activity != null)
				activity.finish();
		}
	}

	/**
	 * finish指定的activity
	 * 
	 * @author blue
	 */
	public boolean finishActivity(Class<? extends Activity> actCls)
	{
		Activity act = findActivityByClass(actCls);
		if (null != act && !act.isFinishing())
		{
			act.finish();
			return true;
		}
		return false;
	}

	public Activity findActivityByClass(Class<? extends Activity> actCls)
	{
		Activity aActivity = null;
		Iterator<Activity> itr = mActicityStack.iterator();
		while (itr.hasNext())
		{
			aActivity = itr.next();
			if (null != aActivity && aActivity.getClass().getName().equals(actCls.getName()) && !aActivity.isFinishing())
			{
				break;
			}
			aActivity = null;
		}
		return aActivity;
	}

	/**
	 * finish指定的activity之上的所有activity
	 * 
	 * @author blue
	 */
	public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf)
	{
		List<Activity> buf = new ArrayList<Activity>();
		int size = mActicityStack.size();
		Activity activity = null;
		for (int i = size - 1; i >= 0; i--)
		{
			activity = mActicityStack.get(i);
			if (activity.getClass().isAssignableFrom(actCls))
			{
				for (Activity a : buf)
				{
					a.finish();
				}
				return true;
			} else if (i == size - 1 && isIncludeSelf)
			{
				buf.add(activity);
			} else if (i != size - 1)
			{
				buf.add(activity);
			}
		}
		return false;
	}
}
