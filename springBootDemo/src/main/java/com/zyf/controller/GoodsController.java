package com.zyf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zyf.dao.GoodsDao;
import com.zyf.entity.Goods;
import com.zyf.service.goodsService;

@RestController
public class GoodsController {
	
	@Autowired
	private goodsService goodsService;
	
	@Autowired
	private GoodsDao goodsDao;
	
	/**
	 * jdbc操作，添加一个商品
	 * @param goodsName
	 * @return
	 */
	@RequestMapping(value="/addGoods", method=RequestMethod.GET)
	public String addGoods(String goodsName) {
		
		goodsService.addGoods(goodsName);
		
		return "success";
	}
	
	/**
	 * jpa操作，通过主键查找商品
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/testJpa", method=RequestMethod.GET)
	public Goods testJpa(String goodsId) {
		
		Goods goods = goodsDao.findOne(goodsId);
		
		return goods;
		
	}
	/**
	 * mybatis操作，通过商品名称查找商品
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/findByName", method=RequestMethod.GET)
	public Goods findByName(String name) {
		return goodsService.findByName(name);
	}
	
	/**
	 * mybatis操作，添加一商品
	 * @param goodsName
	 */
	@RequestMapping(value="/insertOne", method=RequestMethod.GET)
	public void insertOne(String goodsName) {
		goodsService.insertOne(goodsName);
	}
}
