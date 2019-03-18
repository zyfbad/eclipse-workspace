package com.zyf.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获类
 * @author acer
 *
 */
@ControllerAdvice
public class GlobalExceptionHandle {
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Map<String, Object> dealException(){
		
		Map<String, Object> resultmap = new HashMap<>();
		
		resultmap.put("errorCode", "500");
		resultmap.put("errorMessage", "系统错误");
		
		return resultmap;
	}
}
