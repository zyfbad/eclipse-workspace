package com.zyf.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zyf.demo.TopicProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-jms-producer.xml")
public class TopicTest {

	@Autowired
	private TopicProducer topicProducer;
	
	@org.junit.Test
	public void testSendMessage() {
		topicProducer.sendTextMessage("springjsm 发布/订阅");
	}
}
