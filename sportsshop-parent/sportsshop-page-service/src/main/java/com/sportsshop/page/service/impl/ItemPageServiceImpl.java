package com.sportsshop.page.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.sportsshop.mapper.TbGoodsDescMapper;
import com.sportsshop.mapper.TbGoodsMapper;
import com.sportsshop.mapper.TbItemCatMapper;
import com.sportsshop.mapper.TbItemMapper;
import com.sportsshop.mapper.TbSpecificationMapper;
import com.sportsshop.page.service.ItemPageService;
import com.sportsshop.pojo.TbGoods;
import com.sportsshop.pojo.TbGoodsDesc;
import com.sportsshop.pojo.TbItem;
import com.sportsshop.pojo.TbItemCat;
import com.sportsshop.pojo.TbItemExample;
import com.sportsshop.pojo.TbItemExample.Criteria;
import com.sportsshop.pojogroup.Specification;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService{

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	
	@Autowired
	private TbItemMapper itemMapper;
	
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
			//1.商品主表的数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			
			//2.商品拓展表的数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			//3.商品的三级分类
			String category1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String category2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String category3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("category1", category1);
			dataModel.put("category2", category2);
			dataModel.put("category3", category3);
			
			
			//4.获取sku列表
			TbItemExample example = new TbItemExample();
			TbItemExample.Criteria criteria = example.createCriteria();
			
			criteria.andStatusEqualTo("1"); //查找有效的sku列表
			criteria.andGoodsIdEqualTo(goodsId); //查找当前商品id的sku列表
			
			example.setOrderByClause("is_default desc"); //按照是否默认的状态值来进行排序，保证第一个是默认的sku
			
			List<TbItem> itemList = itemMapper.selectByExample(example);
			dataModel.put("itemList", itemList);
			
			
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
