package com.sportsshop.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
	
	/**
	 * s搜索item
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> search(Map searchMap);
	
	/**
	 * 导入item数据
	 */
	public void importList(List list);
	
	/**
	 * 清除数据
	 * 1.但删除商品等时
	 */
	public void deleteByGoodsIds(List goodsIdsList);
}
