package com.sportsshop.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsshop.mapper.TbBrandMapper;
import com.sportsshop.mapper.TbGoodsDescMapper;
import com.sportsshop.mapper.TbGoodsMapper;
import com.sportsshop.mapper.TbItemCatMapper;
import com.sportsshop.mapper.TbItemMapper;
import com.sportsshop.mapper.TbSellerMapper;
import com.sportsshop.pojo.TbBrand;
import com.sportsshop.pojo.TbGoods;
import com.sportsshop.pojo.TbGoodsDesc;
import com.sportsshop.pojo.TbGoodsExample;
import com.sportsshop.pojo.TbGoodsExample.Criteria;
import com.sportsshop.pojo.TbItem;
import com.sportsshop.pojo.TbItemCat;
import com.sportsshop.pojo.TbItemExample;
import com.sportsshop.pojo.TbSeller;
import com.sportsshop.pojogroup.Goods;
import com.sportsshop.sellergoods.service.GoodsDescService;
import com.sportsshop.sellergoods.service.GoodsService;
import com.sun.tools.jdi.VoidTypeImpl;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbBrandMapper brandMapper;
	
	@Autowired
	private TbSellerMapper sellerMapper;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		
		goods.getGoods().setAuditStatus("0");	//状态未审核
		goodsMapper.insert(goods.getGoods());	//插入商品基本信息
		
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId()); //获取外键
		goodsDescMapper.insert(goods.getGoodsDesc()); //插入商品拓展信息
		
		saveItemList(goods); //插入sku数据
		
	}
	
	private void setItemProperty(TbItem item, Goods goods){
		//商品分类
		item.setCategoryid(goods.getGoods().getCategory3Id());
		item.setCreateTime(new Date());
		item.setUpdateTime(new Date());
		
		item.setGoodsId(goods.getGoods().getId()); //商品ID
		item.setSellerId(goods.getGoods().getSellerId());   //商家ID
		
		//商品分类
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		
		//品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		
		//商家名称
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		
		//图片名称
		List<Map> list = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
		if(list.size()>0) {
			item.setImage( (String)(list.get(0)).get("url"));
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		
		//更新基本表数据
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		
		//更新拓展表数据
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
		
		/*删除原有的SKU数据*/
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		//插入新的SKU列表
		saveItemList(goods); //插入sku数据
	}	
	
	/**
	 * 插入的重复代码
	 * 
	 */
	public void saveItemList(Goods goods) {
		if("1".equals(goods.getGoods().getIsEnableSpec())) {
			//启用规格
			for(TbItem item: goods.getItemList()) {
				
				//构建标题 spu+规格选项值
				String title = goods.getGoods().getGoodsName(); //spu
				Map<String, Object> map = JSON.parseObject(item.getSpec());
				for(String key: map.keySet()) {
					title += " "+map.get(key);  //添加规格选项值
				}
				item.setTitle(title);
				
				setItemProperty(item, goods);
				
				itemMapper.insert(item);
			}
		}else {
			//不启用规格
			TbItem item = new TbItem();
			
			item.setTitle(goods.getGoods().getGoodsName());
			item.setPrice(goods.getGoods().getPrice());
			item.setNum(999);
			item.setStatus("1");
			item.setSpec("{}");
			item.setIsDefault("1");
			
			setItemProperty(item, goods);
			
			itemMapper.insert(item);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		
		Goods goods = new Goods();
		
		//获取商品基本表
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		
		//获取商品拓展表
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		
		//获取商品规格表
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> itemslist = itemMapper.selectByExample(example);
		
		goods.setItemList(itemslist);
		
		return goods;
		
	}
	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1"); //逻辑删除
			goodsMapper.updateByPrimaryKey(goods);
			
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andIsDeleteIsNull(); //设置必须为null
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(Long[] ids, String status) {
		
		for(Long id: ids) {
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(goods);
		}
	}
	
}
