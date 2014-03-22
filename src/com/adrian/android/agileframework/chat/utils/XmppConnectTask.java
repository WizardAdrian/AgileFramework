package com.adrian.android.agileframework.chat.utils;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.adrian.android.agileframework.abstracts.WeakAsyncTask;

public class XmppConnectTask extends
		WeakAsyncTask<Integer, Integer, Integer, Context> {
	private int code;
	private String uid;
	private int count = 3;
	private Handler mHandler = new Handler();
	Runnable mRunnable = new Runnable() {
		public void run() {
			if (count < 0) {
				XmppTool.xmppConnect(mTarget.get());
				count = 3;
				return;
			}
			Log.e("reconnect after ", count + " second(s)");
			count--;
			mHandler.postDelayed(mRunnable, 1 * 1100);
		}
	};

	public XmppConnectTask(Context target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Integer doInBackground(Context target, Integer... params) {
		// TODO Auto-generated method stub
		try {
			String md5String = MD5Tools.MD5(uid);
			if (!TextUtils.isEmpty(md5String)) {
				if (md5String.length() >= 24) {
					String pwd = md5String.substring(5, 29);
					XmppTool.getConnection().login(uid, pwd);
				}
			}

			// OfflineMessageUtil.offlinemsg((Activity) target);
			// status
			Presence presence = new Presence(Presence.Type.available);
			XmppTool.getConnection().sendPacket(presence);
			code = 1;
		} catch (XMPPException e) {
			code = -1;
			Log.e("XMPPException", e.getMessage());
		} catch (IllegalStateException e) {
			if ("Already logged in to server.".equals(e.getMessage())) {
				code = 1;
			} else {
				code = -2;
			}
			e.printStackTrace();
			Log.e("IllegalStateException", e.getMessage());
		} catch (NullPointerException e) {
			code = -3;
			e.printStackTrace();
		}
		return code;
	}

	@Override
	protected void onPostExecute(Context target, Integer result) {
		// TODO Auto-generated method stub
		if (result == 1) {
			
		} else {

		}
	}
}
