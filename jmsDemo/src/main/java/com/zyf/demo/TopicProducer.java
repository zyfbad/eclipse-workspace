package com.zyf.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer {

	public static void main(String[] args) throws JMSException {
		
		//1.连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.136:61616");
		
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		//3.启动连接
		connection.start();
		
		//4.创建会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.创建topic对象
		Topic topic = session.createTopic("test-topic");
		
		//6.创建生产者对象
		MessageProducer producer = session.createProducer(topic);
		
		//7.文本消息对象
		TextMessage textMessage = session.createTextMessage("欢迎来到jms的世界");
		
		//8.发消息
		producer.send(textMessage);
		
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}

}
