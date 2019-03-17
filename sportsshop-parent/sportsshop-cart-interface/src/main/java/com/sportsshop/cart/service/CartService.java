package com.sportsshop.cart.service;

import java.util.List;

import com.sportsshop.pojo.TbUser;
import com.sportsshop.pojogroup.Cart;

public interface CartService {
	
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	public List<Cart> findCartListFromRedis(Long userId);
	
	public void saveCartListToRedis(Long userId, List<Cart> cartList);
}
