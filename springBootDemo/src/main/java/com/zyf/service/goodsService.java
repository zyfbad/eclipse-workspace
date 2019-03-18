package com.zyf.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.zyf.entity.Goods;
import com.zyf.mapper.GoodsMapper;

@Service
public class goodsService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	/**
	 * jdbc操作
	 * @param goodsName
	 */
	public void addGoods(String goodsName) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		String goodId = dateFormat.format(now);
		
		jdbcTemplate.update("insert into goodmessage values(?,?,?,?,?);", goodId, goodsName, 500, 650, 1000);
		
	}
	
	/**
	 * mybaits操作
	 * @param name
	 * @return
	 */
	public Goods findByName(String name) {
		return goodsMapper.findByName(name);
	}
	
	/**
	 * mybaits操作
	 * @param name
	 * @return
	 */
	public void insertOne(String goodsName) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		String goodsId = dateFormat.format(now);
		
		goodsMapper.insertOne(goodsId, goodsName, 800, 980, 199);
		
	};
}
