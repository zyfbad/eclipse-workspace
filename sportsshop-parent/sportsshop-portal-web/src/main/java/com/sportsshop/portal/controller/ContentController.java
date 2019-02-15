package com.sportsshop.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sportsshop.content.service.ContentService;
import com.sportsshop.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {
	
	@Reference
	private ContentService contentService;
	
	/**
	 * 根据广告类别ID查找广告列表
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/findByCategoryId")
	public List<TbContent> findByCategoryId(Long categoryId) {
		
		return contentService.findByCategoryId(categoryId);
		
	}
	
}
