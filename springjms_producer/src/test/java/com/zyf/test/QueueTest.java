package com.zyf.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zyf.demo.QueueProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-jms-producer.xml")
public class QueueTest {

	@Autowired
	private QueueProducer queueProducer;
	
	@org.junit.Test
	public void testSendMessage() {
		queueProducer.sendTextMessage("springjsm 点对点");
	}
}
