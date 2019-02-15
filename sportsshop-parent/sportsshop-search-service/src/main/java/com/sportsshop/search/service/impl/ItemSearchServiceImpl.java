package com.sportsshop.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.Join;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.sportsshop.pojo.TbBrand;
import com.sportsshop.pojo.TbContent;
import com.sportsshop.pojo.TbItem;
import com.sportsshop.pojo.TbSpecification;
import com.sportsshop.search.service.ItemSearchService;


@Service
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * g关键字搜索
	 */
	public Map<String, Object> search(Map searchMap) {
		
		Map<String, Object> totalMap = new HashMap<>();
		
		//高亮显示查询结果暂未实现
		totalMap.putAll(searchByKeywords(searchMap));
		
		//查询品牌分类列表
		List categoryList = searchCategoryList(searchMap);
		totalMap.put("categoryList", categoryList);
		
		String categoryName = (String) searchMap.get("category");
		if(null != categoryName && !"".equals(categoryName)) {
			totalMap.putAll(searchBrandAndSpecList(categoryName));
		}else {
			if(categoryList.size()>0) {
				totalMap.putAll(searchBrandAndSpecList((String)categoryList.get(0)));
			}
		}
		return totalMap;
	}
	
	/**
	 * g根据关键字查询
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> searchByKeywords(Map searchMap){
		Map<String, Object> totalMap = new HashMap<>();
		
		Query query = new SimpleQuery("*:*");
		
		System.out.println(searchMap.get("keywords"));
		//增加查询条件-关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//按分类查询
		if(!"".equals(searchMap.get("category"))) {
			Criteria categoryFilterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			FilterQuery filterQuery = new SimpleQuery(categoryFilterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//按品牌查询
		if(!"".equals(searchMap.get("brand"))) {
			Criteria brandFilterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			FilterQuery filterQuery = new SimpleQuery(brandFilterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//规格查询
		if(searchMap.get("spec")!=null) {
			Map<String, String> specMap = (Map) searchMap.get("spec");
			for(String key: specMap.keySet()) {
				Criteria specFilterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
				FilterQuery filterQuery = new SimpleQuery(specFilterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		
		List<TbItem> itemList = page.getContent();
		for(TbItem item : itemList) {
			System.out.println(item.getTitle());
		}
		totalMap.put("rows", page.getContent());
		
		return totalMap;
	}
	
	
	public List searchCategoryList(Map searchMap) {
		
		List list = new ArrayList<>();
		
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		
		Page<GroupEntry<TbItem>> entityPage = groupResult.getGroupEntries();
		
		List<GroupEntry<TbItem>> entityList = entityPage.getContent();
		
		for(GroupEntry<TbItem> entity: entityList) {
			list.add(entity.getGroupValue());
		}
		return list;
	}
	
	private Map  searchBrandAndSpecList(String category) {
		Map<String, Object> map = new HashMap<>();
		
		//获取模板ID
		Long typeId = (Long)redisTemplate.boundHashOps("itemCat").get(category);
		
		//如果模板ID不为空
		if(typeId != null) {
			//获取品牌列表
			List<TbBrand> brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);
			//获取规格列表
			List<TbSpecification> specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
		}
		return map;
	}
}
