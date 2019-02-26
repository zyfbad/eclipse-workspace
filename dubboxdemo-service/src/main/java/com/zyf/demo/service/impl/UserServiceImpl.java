package com.zyf.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zyf.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public String getName() {
		return "zyf";
	}
}
