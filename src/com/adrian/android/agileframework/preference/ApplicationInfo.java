package com.adrian.android.agileframework.preference;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.adrian.android.agileframework.bean.Bean_Application;
import com.adrian.android.agileframework.utils.Constants;
import com.alibaba.fastjson.JSON;

public class ApplicationInfo {
	public static final String ACCOUNT_PREFERENCE = "com.closingtools.android";

	private static ApplicationInfo M_TASK;

	public Activity mActivity;
	public Context mContext;
	private Bean_Application application;
	public SimpleDateFormat formatNormal;

	private ApplicationInfo(Context mContext) {
		if (mContext != null)
			this.mContext = mContext.getApplicationContext();

		formatNormal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static ApplicationInfo get(Context mContext) {
		if (M_TASK == null) {
			M_TASK = new ApplicationInfo(mContext);
		}
		return M_TASK;
	}

	public Bean_Application getProjectInfo() {
		if (application == null) {
			getDataFromPreferences();
		}
		return application;
	}

	private void getDataFromPreferences() {
		SharedPreferences settings = mContext.getSharedPreferences(
				ACCOUNT_PREFERENCE, Context.MODE_PRIVATE);

		Bean_Application info = JSON.parseObject(
				settings.getString(Constants.Key.APPLICATION, null),
				Bean_Application.class);

		if (info == null) {
			application = new Bean_Application();
		} else {
			application = info;
		}
		settings = null;
	}

	public void saveDataToPreferences() {
		if (application != null) {
			SharedPreferences settings = mContext.getSharedPreferences(
					ACCOUNT_PREFERENCE, Context.MODE_PRIVATE);
			Editor edit = settings.edit();

			String json_string = JSON.toJSONString(application);

			edit.putString(Constants.Key.APPLICATION, json_string);

			edit.commit();
			edit.clear();
			edit = null;
			settings = null;
		}
	}

	public void clearMyConstants() {
		application = new Bean_Application();
		this.saveDataToPreferences();
	}

	public void setPrjInfo(Bean_Application _application) {
		application = _application;
		saveDataToPreferences();
	}
}
