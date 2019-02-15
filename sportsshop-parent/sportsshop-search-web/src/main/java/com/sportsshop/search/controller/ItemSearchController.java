package com.sportsshop.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sportsshop.search.service.ItemSearchService;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {
	
	@Reference
	private ItemSearchService itemSearchService;

	@RequestMapping("/search")
	public Map<String, Object> search(@RequestBody Map searchMap){
		System.out.println("controller  " + searchMap.get("keywords"));
		return itemSearchService.search(searchMap);
		
	}
}
