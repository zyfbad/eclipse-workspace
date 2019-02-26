package com.sportsshop.search.service.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sportsshop.pojo.TbItem;
import com.sportsshop.search.service.ItemSearchService;

@Component
public class itemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage)message;
		System.out.println("监听到消息：\n"+textMessage);
		
		try {
			String jsonString = textMessage.getText();
			List<TbItem> itemList = JSON.parseArray(jsonString, TbItem.class);
			
			itemSearchService.importList(itemList);
			System.out.println("导入到索引库中了");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
