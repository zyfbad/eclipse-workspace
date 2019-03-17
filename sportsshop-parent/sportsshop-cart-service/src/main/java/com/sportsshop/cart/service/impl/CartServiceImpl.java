package com.sportsshop.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.sportsshop.cart.service.CartService;
import com.sportsshop.mapper.TbItemMapper;
import com.sportsshop.pojo.TbItem;
import com.sportsshop.pojo.TbOrder;
import com.sportsshop.pojo.TbOrderItem;
import com.sportsshop.pojogroup.Cart;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		//1.根据itemId查找sku商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if(item == null) {
			throw new RuntimeException("未找到该id的sku信息");
		}
		if(!"1".equals(item.getStatus())) {
			throw new RuntimeException("该sku的状态不为1");
		}
		//2.获得商家Id
		String sellerId = item.getSellerId();
		System.out.println("====================="+sellerId);
		//3.根据商家id判断购物车列表中是否有该商家
		Cart cart = searchCartBySellerid(cartList, sellerId);
		if(cart==null) { //表示购物车中不存在该id的商家
			//新建该商家的购物车对象，并赋值商家id和名称，和新添加的商品
			
			//详细的列表
			List<TbOrderItem> orderItemList = new ArrayList<>();
			//根据sku和数量创建详细对象
			TbOrderItem orderItem = createOrderItem(item, num);
			orderItemList.add(orderItem );
			Cart newCart = new Cart(sellerId, item.getSeller(), orderItemList);
			cartList.add(newCart);

			System.out.println("=====================没找到商家");
			
		}else {//表示购物车中存在该id的商家
			//1.判断购物车中是否已经存在该商品了
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			
			TbOrderItem orderItem = searchItemByItemid(orderItemList, item.getId());
			if(orderItem == null) {//如果还没有买过该商品item，则添加
				orderItem = createOrderItem(item, num);
				orderItemList.add(orderItem);
				System.out.println("=====================没找到sku");
			}else { //如果买过该商品item，则更新数量等信息
				//在原有的数量上加上新添加的数量
				orderItem.setNum(orderItem.getNum() + num);
				
				if(orderItem.getNum()<=0) {//该明细的都清空了
					orderItemList.remove(orderItem);
					System.out.println("=====================清空该商家");
				}else {
					//更新数量后，重新计算价格
					orderItem.setTotalFee(new BigDecimal(orderItem.getNum()*orderItem.getPrice().doubleValue()));
					System.out.println("=====================重新计算价格");
				}
				
				if(cart.getOrderItemList().size()==0){//该商家的都清空了
					cartList.remove(cart);
					System.out.println("=====================该商家的都清空了");
				}
			
			}
			
		}
		return cartList;
		
	}
	
	private TbOrderItem createOrderItem(TbItem item, Integer num) {
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPrice(item.getPrice());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
		
		orderItem.setPicPath(item.getImage());
		
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		
		return orderItem;
	}

	//根据商家id判断购物车列表中是否有该商家
	public Cart searchCartBySellerid(List<Cart> cartList, String sellerId) {
		
		//遍历购物车列表
		for(Cart cart: cartList) {
			if(cart.getSellerId().equals(sellerId)) {
				return cart;
			}
		}
		return null;
	}
	
	public TbOrderItem searchItemByItemid(List<TbOrderItem> orderItemList, Long itemId) {
		
		//遍历该商家的购物车明细
		for(TbOrderItem item: orderItemList) {
			if(itemId.longValue() == item.getItemId().longValue()) {
				return item;
			}
		}
		return null;
	}

	private RedisTemplate redisTemplate;

	/**
	 * 从缓存中查数据
	 */
	@Override
	public List<Cart> findCartListFromRedis(Long userId) {
		String userIdStr = userId+"";
		
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(userIdStr);
		
		if(cartList == null) {
			return new ArrayList<>();
		}
		
		return cartList;
	}

	/**
	 * 将数据写入缓存
	 */
	@Override
	public void saveCartListToRedis(Long userId, List<Cart> cartList) {
		redisTemplate.boundHashOps("cartList").put(userId+"", cartList);
	}
	


}
