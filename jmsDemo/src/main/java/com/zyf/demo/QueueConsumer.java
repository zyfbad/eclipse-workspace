package com.zyf.demo;



import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueConsumer {

	public static void main(String[] args) throws JMSException, IOException {

		//1.创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.136:61616");
	
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		//3.启动连接
		connection.start();
		
		//4.获得session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.获得queue对象
		Queue queue = session.createQueue("test-queue");
		
		//6.创建消费者对象
		MessageConsumer messageConsumer = session.createConsumer(queue);
		
		//7.设置监听
		messageConsumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println("提取的消息："+textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		//8.等待键盘输入
		System.in.read();
		
		
		//9.关闭资源
		messageConsumer.close();
		session.close();
		connection.close();
	}

}
