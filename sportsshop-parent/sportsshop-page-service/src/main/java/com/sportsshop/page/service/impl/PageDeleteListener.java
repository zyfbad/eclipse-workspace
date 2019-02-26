package com.sportsshop.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportsshop.page.service.ItemPageService;

@Component
public class PageDeleteListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService; 
	
	@Override
	public void onMessage(Message message) {
		
		//转换
		ObjectMessage objectMessage = (ObjectMessage)message;
		
		//强转
		try {
			Long[] goodsIds = (Long [])objectMessage.getObject();
			
			System.out.println("商品详细页接收到的goodIds信息为: "+goodsIds);
			
			Boolean result = itemPageService.deleteItemHtml(goodsIds);
			System.out.println("删除结果："+result);
			
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
		
	}

}
