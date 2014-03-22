package com.adrian.android.agileframework.chat.utils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

public class OfflineMessageUtil {
	public static void offlinemsg(Activity mActivity) {
		OfflineMessageManager offlineManager = new OfflineMessageManager(
				XmppTool.getConnection());
		Iterator<org.jivesoftware.smack.packet.Message> it;
		try {
			it = offlineManager.getMessages();
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message message = it.next();
				String contentString;
				try {
					contentString = new String(message.getBody(
							ConstantsChat.MSG_TEXT).getBytes(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					contentString = message.getBody(ConstantsChat.MSG_TEXT);
				}
				String xmmppJID = message.getFrom().split("@")[0];
				if (!TextUtils.isEmpty(xmmppJID)) {
					
					Log.e("saveToDB", "offlinemsg");
				}
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			offlineManager.deleteMessages();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
