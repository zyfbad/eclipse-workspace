package com.sportsshop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sportsshop.pojo.TbUser;
import com.sportsshop.user.service.UserService;
/**
 * 认证类
 * @author acer
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("经过了普通用户的loadUserByUsername");
		
		//构建一个角色的列表
		List<GrantedAuthority> grantAuth = new ArrayList();
		grantAuth.add(new SimpleGrantedAuthority("ROLE_USER"));
		//转化userId，由string变为Long
		Long userId = Long.parseLong(username);

		//查找用户对象
		TbUser user = userService.findOne(userId);

		if(user != null) {
			if(user.getStatus().equals("1")) {
				System.out.println("找到此人");
				return new User(username, user.getPassword(), grantAuth);
			}else {
				System.out.println("非正常状态！");
				return null;
			}
		}else {
			System.out.println("查无此人！");
			return null;
		}
	}
}
