package com.zyf.demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer {

	@JmsListener(destination="zyfDestination")
	public void readMessage(String text) {
		System.out.println("接收到的消息: "+text);
	}
}
