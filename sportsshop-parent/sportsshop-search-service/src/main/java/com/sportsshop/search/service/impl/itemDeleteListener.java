package com.sportsshop.search.service.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportsshop.search.service.ItemSearchService;

@Component
public class itemDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage)message;
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			System.out.println("接收到ids信息为："+ids);
			
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			System.out.println("成功删除索引库中的记录");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
