package com.hc.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		String text = null;
		try {
			text = textMessage.getText();
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		System.out.println(text);
	}

}
