package com.zyf.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener{

	public void onMessage(Message message) {
		//强转
		TextMessage textMessage = (TextMessage) message;
		
		try {
			System.out.println("接收到的信息是："+textMessage.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
