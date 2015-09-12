package com.testcar.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.testcar.application.LocalApplication;

/**
 * Post请求
 * 
 * @author blue
 * 
 */
public class AsyncHttpPost
{
	// 线程通信
	private Handler handler;

	// 访问路径
	private String url = "";

	// 请求参数
	private RequestParams params = new RequestParams();

	public AsyncHttpPost(Handler handler)
	{
		this.handler = handler;
	}

	// 设置请求参数
	public void setParams(String account, String password)
	{
		params.addBodyParameter("account", account);
		params.addBodyParameter("password", password);
	}

	// 登录验证
	public void loginCheck()
	{
		LocalApplication.getInstance().httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				// 回送消息
				handler.sendEmptyMessage(-1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				// 回送消息
				Message message = handler.obtainMessage(1, analysisData(arg0.result));
				handler.sendMessage(message);
			}
		});
	}

	// 解析json
	public int analysisData(String resultJson)
	{
		// 对json结果进行处理，避免干扰字符
		resultJson = resultJson.substring(resultJson.indexOf("{"), resultJson.lastIndexOf("}") + 1);

		JSONObject jsonObject = JSONObject.parseObject(resultJson);
		int result = jsonObject.getInteger("result");

		return result;
	}
}
