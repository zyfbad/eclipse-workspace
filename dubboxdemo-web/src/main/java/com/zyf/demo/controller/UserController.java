package com.zyf.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyf.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//远程注入
	@Reference
	private UserService userService;
	
	
	@RequestMapping("/showName")
	@ResponseBody //不加会表示返回页面,加了表示返回返回值
	public String showName() {
		return userService.getName();
	}
}
