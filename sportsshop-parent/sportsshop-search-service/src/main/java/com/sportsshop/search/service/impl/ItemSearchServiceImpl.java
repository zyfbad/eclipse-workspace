package com.sportsshop.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
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
		
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replaceAll(" ", ""));
		//关键字查询
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
		
		//价格查询
		String priceStr = (String) searchMap.get("price"); //获取价格字符串
		if(priceStr!=null && !"".equals(priceStr)) {
			String price[] = priceStr.split("-");
			if(price.length==2) {
				if(!"".equals(price[0])) { //如果开始区间不为0
					Criteria priceFilterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
					FilterQuery filterQuery = new SimpleQuery(priceFilterCriteria);
					query.addFilterQuery(filterQuery);
				}
				
				if(!"*".equals(price[1])) { //如果结束区间不为*
					Criteria priceFilterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
					FilterQuery filterQuery = new SimpleQuery(priceFilterCriteria);
					query.addFilterQuery(filterQuery);
				}
			}
		}
		
		//分页处理
		//获取当前页码
		Integer pageNo = (Integer) searchMap.get("pageNo");
		if(null == pageNo) {
			pageNo = 1;  //如果前端没有传值则默认为1
		}
		//获取每页数量
		Integer pageSize = (Integer) searchMap.get("pageSize");
		if(null == pageSize) {
			pageSize = 20;  //如果前端没有传值则默认为20
		}
		query.setOffset(pageSize*(pageNo-1));   //设置起始值
		query.setRows(pageSize);  //设置每页值
		
		
		//排序
		String sortField = (String)searchMap.get("sortField"); //排序字段
		String sortValue = (String)searchMap.get("sortValue");  //排序值 ASC DESC
		
		if(null!=sortValue && !"".equals(sortValue)) {
			if("ASC".equals(sortValue)) { //升序
				Sort sort = new Sort(Sort.Direction.ASC, "item_"+sortField);
				query.addSort(sort );
			}else if("DESC".equals(sortValue)) {//降序
				Sort sort = new Sort(Sort.Direction.DESC, "item_"+sortField);
				query.addSort(sort );
			}else {
				
			}
		}
		
		//获取结果
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		
		List<TbItem> itemList = page.getContent();
		for(TbItem item : itemList) {
			System.out.println(item.getTitle());
		}
		totalMap.put("rows", page.getContent());
		totalMap.put("totalPages", page.getTotalPages());  //获取总的分页数
		totalMap.put("totalElements", page.getTotalElements()); //获取总记录数
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
	
	/**
	 * 将item列表数据装入solr
	 */
	@Override
	public void importList(List list) {
		System.out.print("searchService中，");
		System.out.println("将"+list.size()+"条item数据更新到solr");
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}

	/**
	 * 根据商品id从solr中删除数据
	 */
	@Override
	public void deleteByGoodsIds(List goodsIdsList) {
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_goodsid").in(goodsIdsList);
		query.addCriteria(criteria);
		solrTemplate.delete(query );
		solrTemplate.commit();
	}
}
