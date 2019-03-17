package com.sportsshop.cart.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.sportsshop.cart.service.CartService;
import com.sportsshop.pojogroup.Cart;

import entity.Result;
import util.CookieUtil;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Reference
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){
		String cartListStr = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		
		if(cartListStr==null || "".equals(cartListStr)) {
			cartListStr = "[]";
		}
		
		//将cookie中的购物车列表从string转化为List
		List<Cart> cartListCookie = JSON.parseArray(cartListStr, Cart.class);
		
		return cartListCookie;
	} 
	
	@RequestMapping("/addToCartList")
	public Result addToCartList(Long itemId, Integer num){
		try {
			//先获取cookie中的列表
			List<Cart> cartList = this.findCartList();
			
			//将新选购的商品加入到加入购物车列表中
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			
			System.out.println(cartList.size());
			//将新的购物车列表保存到cookie中
			CookieUtil.setCookie(request, response, "cartList", 
					JSON.toJSONString(cartList),3600*24,"UTF-8");
			
			return new Result(true, "加入购物车成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "加入购物车失败");
		}
	}
	
}
