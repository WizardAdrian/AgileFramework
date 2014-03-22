//+---------------------------------------------------------------------------+
//| Copyright (c) 2013 - present Wayma Group, Inc.                                      |
//| All rights reserved.                                                      |
//|                                                                           |
//+---------------------------------------------------------------------------+
//| For help with this library, contact contact@wayma_group.com               |
//+---------------------------------------------------------------------------+
package com.adrian.android.agileframework.interfaces;

import com.alibaba.fastjson.JSONException;

public interface IHandleData {

	/** when the task call onSuccess */
	public void handleData(String jsonString) throws JSONException;

	/** when the task call onFailure */
	public void handleFailure();
}
