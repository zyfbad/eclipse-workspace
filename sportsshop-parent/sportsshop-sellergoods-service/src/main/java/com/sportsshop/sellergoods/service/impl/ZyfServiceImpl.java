package com.sportsshop.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.sportsshop.sellergoods.service.ZyfService;
@Service
public class ZyfServiceImpl implements ZyfService {

	@Override
	public String showName() {
		
		return "zyf";
		
	}

}
