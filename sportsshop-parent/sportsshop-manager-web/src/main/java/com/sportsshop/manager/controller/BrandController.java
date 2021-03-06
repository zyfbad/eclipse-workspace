package com.sportsshop.manager.controller;

import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchControls;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sportsshop.pojo.TbBrand;
import com.sportsshop.sellergoods.service.BrandService;
/**
 * x相当于responseBody+Controller
 * @author acer
 *
 */

import entity.PageResult;
import entity.Result;
@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();
	}
	
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int size) {
		return brandService.findPage(page, size);
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand) {
		try {
			brandService.add(brand);
			return new Result(true, "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "新增失败");
		}
	}
	
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id) {
		
		return brandService.findOne(id);
		
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand) {
		try {
			brandService.update(brand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	@RequestMapping("/delete")
	public Result del(Long[] ids) {
		
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
		
	}
	
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand, int page, int size) {
		return brandService.findPage(brand, page, size);
	}
	
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
}
