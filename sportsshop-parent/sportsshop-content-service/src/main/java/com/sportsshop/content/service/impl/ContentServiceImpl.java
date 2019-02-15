package com.sportsshop.content.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsshop.mapper.TbContentMapper;
import com.sportsshop.pojo.TbContent;
import com.sportsshop.pojo.TbContentExample;
import com.sportsshop.pojo.TbContentExample.Criteria;
import com.sportsshop.content.service.ContentService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;	
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);		
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		//之前的类别
		Long categoryId = (contentMapper.selectByPrimaryKey(content.getId())).getCategoryId();
		redisTemplate.boundHashOps("content").delete(categoryId);

		
		contentMapper.updateByPrimaryKey(content);
		
		//如果更改后的和更改前的类别不同，则需要
		if(categoryId.longValue() != content.getCategoryId().longValue()) {
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			Long categoryId = (contentMapper.selectByPrimaryKey(id)).getCategoryId();
			redisTemplate.boundHashOps("content").delete(categoryId);
			
			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	
	/**
	 * 根据广告分类ID查找广告列表
	 * 
	 */
	public List<TbContent> findByCategoryId(Long categoryId) {
		
		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		
		if(contentList == null) {
			
			System.out.println("从数据库中拿数据");
			
			TbContentExample contentExample = new TbContentExample();
			com.sportsshop.pojo.TbContentExample.Criteria criteria = contentExample.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");//开启状态
			contentExample.setOrderByClause("sort_order");//排序
			
			contentList =  contentMapper.selectByExample(contentExample);
			
			redisTemplate.boundHashOps("content").put(categoryId, contentList); //存入缓存中
			
		}else {
			
			System.out.println("从缓存中拿数据");
			
		}
		
		return contentList;
		
	}
	
}
