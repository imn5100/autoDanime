package com.shaw.test;

import java.io.IOException;
import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class Xmpptest {

	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(new XmppThread("imn5100sama", "*********", "五更琉璃@nekonazo.com"));
		thread.start();
	}
}

class XmppThread implements Runnable {
	private XMPPTCPConnectionConfiguration.Builder configBuilder;
	private AbstractXMPPConnection connection;
	private String friend;
	ChatManager chatmanager;;

	XmppThread(String username, String password, String friend) {
		this.friend = friend;
		configBuilder = XMPPTCPConnectionConfiguration.builder();
		configBuilder.setUsernameAndPassword(username, password);
		configBuilder.setServiceName("nekonazo.com");
		configBuilder.setSecurityMode(SecurityMode.disabled);
		configBuilder.setDebuggerEnabled(true);
		connection = new XMPPTCPConnection(configBuilder.build());
		try {
			connection.connect();
			connection.login();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		chatmanager = ChatManager.getInstanceFor(connection);

	}

	public void reConnection(String username, String password) throws Exception {
		connection.login(username, password);
	}

	public void run() {
		try {
			Chat newChat = chatmanager.createChat(friend, new ChatMessageListenerImpl());
			newChat.sendMessage("你好");
			while (true) {
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				String msg = sc.next();
				if (msg.equals("exit-java"))
					System.exit(0);
				newChat.sendMessage(msg);
			}
		} catch (Exception e) {
			System.out.println("登录或连接失败");
			e.printStackTrace();
		}
	}

	class ChatMessageListenerImpl implements ChatMessageListener {
		@Override
		public void processMessage(Chat chat, Message message) {
			System.out.println(chat.getParticipant() + ":" + message.getBody());
		}
	}

}