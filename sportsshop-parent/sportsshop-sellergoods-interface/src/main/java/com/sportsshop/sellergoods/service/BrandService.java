package com.sportsshop.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.sportsshop.pojo.TbBrand;

import entity.PageResult;

/**
 * d品牌接口
 * @author acer
 *
 */
public interface BrandService {
	public List<TbBrand> findAll();
	
	/**
	 * 品牌分页
	 * @param pageNum 单前页
	 * @param pageSize 单前页记录
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	/**
	 * 增加商品
	 * @param brand
	 */
	public void add(TbBrand brand);
	
	/**
	 * 根据id查找单个品牌
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	  * 修改brand
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	/**
	 * 删除品牌
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	/**
	 * p品牌按条件查询并分页
	 * @param pageNum 单前页
	 * @param pageSize 单前页记录
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

	/**
	 * f返回下拉列表数据
	 * @return
	 */
	public List<Map> selectOptionList();
}
