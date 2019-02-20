package com.sportsshop.page.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.sportsshop.mapper.TbGoodsDescMapper;
import com.sportsshop.mapper.TbGoodsMapper;
import com.sportsshop.page.service.ItemPageService;
import com.sportsshop.pojo.TbGoods;
import com.sportsshop.pojo.TbGoodsDesc;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService{

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Value("${pagedir}")
	private String pagedir;
	
	@Override
	public boolean genItemHtml(Long goodsId) {
		
		Configuration configuration = freeMarkerConfig.getConfiguration();
		
		try {
			//获取模板对象
			Template template = configuration.getTemplate("item.ftl");
			
			//创建数据模型
			Map dataModel = new HashedMap();
			//商品主表的数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			//商品拓展表的数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			
			
			//创建输出流对象
			Writer out = new FileWriter(pagedir+goodsId+".html");
			
			//输出
			template.process(dataModel, out);
			
			//关闭输出流
			out.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
