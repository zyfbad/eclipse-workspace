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

import com.sportsshop.pojo.TbSeller;
import com.sportsshop.sellergoods.service.SellerService;
/**
 * 认证类
 * @author acer
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("经过了loadUserByUsername");
		
		//构建一个角色的列表
		List<GrantedAuthority> grantAuth = new ArrayList();
		grantAuth.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		
		//查找商家对象
		TbSeller seller = sellerService.findOne(username);
		if(seller != null) {
			if(seller.getStatus().equals("1")) {
				return new User(username, seller.getPassword(), grantAuth);
			}else {
				return null;
			}
		}else {
			return null;
		}
	}

}
