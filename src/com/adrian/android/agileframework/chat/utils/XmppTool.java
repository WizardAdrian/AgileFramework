package com.adrian.android.agileframework.chat.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PrivacyList;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.adrian.android.agileframework.ApplicationProject;

public class XmppTool {

	private static XMPPConnection con = null;

	private static final String BLACK_LIST = "black list";

	private static void openConnection() {
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				ConstantsChat.HOST, ConstantsChat.PORT);
		Log.i("Android SDK Level=", Build.VERSION.SDK_INT + "");
		if (Build.VERSION.SDK_INT >= 14) {
			connConfig.setTruststoreType("AndroidCAStore");
			connConfig.setTruststorePassword(null);
			connConfig.setTruststorePath(null);
		} else {
			connConfig.setTruststoreType("BKS");
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			connConfig.setTruststorePath(path);
		}
		connConfig.setReconnectionAllowed(true);
		connConfig.setSecurityMode(SecurityMode.disabled);
		connConfig.setSASLAuthenticationEnabled(false);
		connConfig.setSendPresence(false);
		con = new XMPPConnection(connConfig);
		new Thread() {
			@Override
			public void run() {
				try {
					con.connect();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalThreadStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		} else if (!con.isConnected()) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		if (con != null) {
			if (con.isConnected()) {
				con.disconnect();
			}
			con = null;
		}
	}

	public static void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Open());
		//
		// pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Close());
		//
		// pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Data());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	public static void xmppConnect(final Context mContext) {
		XmppConnectTask mTask = new XmppConnectTask(mContext);
		mTask.execute(0);
		ApplicationProject.XMPP_CONNECTING = true;
	}

	public static boolean addToPrivacyList(String name) { // 添加到黑名单
		try {
			PrivacyListManager privacyManager = PrivacyListManager
					.getInstanceFor(XmppTool.getConnection());
			if (privacyManager == null) {
				return false;
			}
			try {
				PrivacyList[] plists = privacyManager.getPrivacyLists();
				if (plists.length == 0) {// 没有黑名单或是名单中没有列，直接getPrivacyList会出错
					List<PrivacyItem> items = new ArrayList<PrivacyItem>();
					Log.i("", "addToPrivacyList plists.length==0");
					PrivacyItem newitem = new PrivacyItem("jid", false, 100);
					newitem.setValue(name + "@"
							+ XmppTool.getConnection().getServiceName());
					items.add(newitem);

					privacyManager.updatePrivacyList(BLACK_LIST, items);
					privacyManager.setActiveListName(BLACK_LIST);
					return true;
				}
			} catch (ClassCastException e) {
				// TODO: handle exception
			}
			PrivacyList plist = privacyManager.getPrivacyList(BLACK_LIST);
			if (plist != null) {
				String ser = "@" + XmppTool.getConnection().getServiceName();

				List<PrivacyItem> items = plist.getItems();
				for (PrivacyItem item : items) {
					String from = item.getValue().substring(0,
							item.getValue().indexOf(ser));
					Log.i("",
							"addToPrivacyList item.getValue=" + item.getValue());
					if (from.equalsIgnoreCase(name)) {
						items.remove(item);
						break;
					}
				}

				PrivacyItem newitem = new PrivacyItem("jid", false, 100);
				newitem.setValue(name + "@"
						+ XmppTool.getConnection().getServiceName());
				items.add(newitem);
				Log.i("", "addToPrivacyList item.getValue=" + newitem.toXML());
				Log.i("", "deleteFromPrivacyList items size=" + items.size());
				privacyManager.updatePrivacyList(BLACK_LIST, items);
				privacyManager.setActiveListName(BLACK_LIST);

			}
			return true;
		} catch (XMPPException ex) {

		}
		return false;
	}

}
