package com.sportsshop.pojogroup;

import java.io.Serializable;
/**
 * good组合实体类
 * @author acer
 *
 */
import java.util.List;

import com.sportsshop.pojo.TbGoods;
import com.sportsshop.pojo.TbGoodsDesc;
import com.sportsshop.pojo.TbItem;
public class Goods implements Serializable {
	
	private TbGoods goods; //goods商品基本信息
	
	private TbGoodsDesc goodsDesc; //goodsdescprit商品拓展信息
	
	private List<TbItem> itemList; //sku列表

	public Goods(){}

	public Goods(TbGoods goods, TbGoodsDesc goodsDesc, List<TbItem> itemList) {
		this.goods = goods;
		this.goodsDesc = goodsDesc;
		this.itemList = itemList;
	}

	public TbGoods getGoods() {
		return goods;
	}

	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}

	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<TbItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	};
	
	
}
