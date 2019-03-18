package com.zyf.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@RequestMapping("/send")
	public void send(String text) {
		jmsMessagingTemplate.convertAndSend("zyfDestination", text);
	}
	
	@RequestMapping("/sendMap")
	public void sendMap() {
		Map map = new HashMap<>();
		map.put("mobile", "1223434355");
		map.put("content", "发送map对象");
		jmsMessagingTemplate.convertAndSend("des_map", map);
	}
}
