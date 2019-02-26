package com.sportsshop.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportsshop.page.service.ItemPageService;

@Component
public class PageListener implements MessageListener{

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		
		ObjectMessage objectMessage = (ObjectMessage)message;
		
		try {
			Long goodsId = (Long)objectMessage.getObject();
			System.out.println("商品详细页接收到的goodsId信息为： "+goodsId);
			itemPageService.genItemHtml(goodsId);
			System.out.println("商品详细页生成完毕");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
