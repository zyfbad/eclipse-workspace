package com.sportsshop.page.service;
/**
 * 商品详细页服务接口
 * @author acer
 *
 */
public interface ItemPageService {
	/**
	 * 生成商品详细页
	 * @param goodsId
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);
	
	/**
	 * 删除商品详细页
	 * @param goodsIds
	 * @return
	 */
	public boolean deleteItemHtml(Long[] goodsIds);
}
