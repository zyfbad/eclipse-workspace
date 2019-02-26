package com.zyf.demo;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueProducer {
	
	public static void main(String[] args) throws JMSException {
		
		//1.创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.136:61616");
		
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		///3.启动连接
		connection.start();
		
		//4.获得session（会话对象），参数1：是否自动事务，参数2：消息的确认模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
		//5.创建一个队列对象
		Queue queue = session.createQueue("test-queue");
		
		//6.创建生产者对象
		MessageProducer messageProducer = session.createProducer(queue);
		
		//7.创建消息对象
		TextMessage textMessage = session.createTextMessage("欢迎来到jms的世界");
		
		//8.生产者发送消息
		messageProducer.send(textMessage);
		
		//9.关闭
		messageProducer.close();
		session.close();
		connection.close();
	}
}
