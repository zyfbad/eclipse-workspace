package com.zyf.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;

@Controller
public class IndexController {

	@ResponseBody
	@RequestMapping(value="/testGlobalException", method=RequestMethod.GET)
	public String testGlobalException() {
		
		System.out.println("马上进入testGlobalException中的异常");
		int a = 2/0;
		return "testGlobalException";
		
	}
	
	
	@RequestMapping(value="/testFreemarker", method=RequestMethod.GET)
	public ModelAndView testFreemarker() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("username", "zhangsan");
		modelAndView.addObject("age", 23);
		modelAndView.setViewName("index");
		
		return modelAndView;
		
	}
	
	@RequestMapping(value="/testFreemarker2", method=RequestMethod.GET)
	public String testFreemarker2(Map<String, Object> resultMap) {
		
		resultMap.put("username", "zhangsanfeng");
		resultMap.put("age", 23);
	
		return "index";
		
	}
	
}
