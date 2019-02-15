package com.sportsshop.search.service;

import java.util.Map;

public interface ItemSearchService {
	
	/**
	 * s搜索item
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> search(Map searchMap);
	
}
