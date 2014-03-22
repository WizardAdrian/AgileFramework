//+---------------------------------------------------------------------------+
//| Copyright (c) 2013 - present Wayma Group, Inc.                                      |
//| All rights reserved.                                                      |
//|                                                                           |
//+---------------------------------------------------------------------------+
//| For help with this library, contact contact@wayma_group.com               |
//+---------------------------------------------------------------------------+
package com.adrian.android.agileframework.abstracts;

import java.lang.ref.WeakReference;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.util.Log;

import com.adrian.android.agileframework.ApplicationProject;
import com.adrian.android.agileframework.interfaces.IHandleData;

public abstract class BaseHttpTask implements IHandleData {
	protected WeakReference<Context> mWeakReference;
	public StringBuilder mUrl;
	public AjaxParams params;

	public BaseHttpTask(Context mContext) {
		mWeakReference = new WeakReference<Context>(mContext);
		mUrl = new StringBuilder();
		params = new AjaxParams();
	}

	public void buildUrl(String key, String value, boolean firstParam) {
		if (firstParam) {
			mUrl.append("?").append(key).append("=").append(value);
		} else {
			mUrl.append("&").append(key).append("=").append(value);
		}
	}

	public void buildRESTUrl(String key, String value) {
		mUrl.append("/").append(key).append("/").append(value);
	}

	public void excute() {
		if (ApplicationProject.DEBUG) {
			Log.e("mUrl", mUrl.toString());
			Log.e("params", params.toString());
		}
		FinalHttp fh = new FinalHttp();
		fh.post(mUrl.toString(), params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.e("onFailure", String.valueOf(errorNo) + ":" + strMsg);
				handleFailure();
			}

			@Override
			public void onSuccess(String jsonString) {
				super.onSuccess(jsonString);
				handleData(jsonString);
			}
		});
	}
}
