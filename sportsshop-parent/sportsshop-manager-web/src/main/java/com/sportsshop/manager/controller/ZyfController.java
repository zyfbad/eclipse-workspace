package com.sportsshop.manager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sportsshop.sellergoods.service.ZyfService;

@RestController
@RequestMapping("/zyf")
public class ZyfController {
	
	@Reference
	private  ZyfService zyfService;
	

	@RequestMapping("/showName")
	public String showName() {
		return zyfService.showName();
	}
	
	
}
