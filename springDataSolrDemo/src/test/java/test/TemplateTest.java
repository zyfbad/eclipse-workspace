package test;

import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zyf.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class TemplateTest {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Test
	public void addTest() {
		TbItem item = new TbItem();
		
		item.setId(1L);
		item.setBrand("华为");
		item.setCategory("手机");
		item.setGoodsId(1L);
		item.setSeller("华为 2 号专卖店");
		item.setTitle("华为 Mate9");
		item.setPrice(new BigDecimal(2000));
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}
	
	@Test
	public void findOne() {
		TbItem item = solrTemplate.getById(1, TbItem.class);
		
		System.out.println(item.getTitle());
	}
	
	@Test
	public void testDelete() {
		
		solrTemplate.deleteById("1");
		
		solrTemplate.commit();
	}
	
	@Test
	public void testAddList() {
		
		List<TbItem> list = new ArrayList();
		
		for(int i=0; i<100; i++) {
			
			TbItem item = new TbItem();
			item.setId(1L+i);
			item.setBrand("华为");
			item.setCategory("手机");
			item.setGoodsId(1L);
			item.setSeller("华为 2 号专卖店");
			item.setTitle("华为 Mate"+i);
			item.setPrice(new BigDecimal(2000+i));
			
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Test
	public void testPageQuery() {
		
		Query query = new SimpleQuery("*:*");
		
		query.setOffset(20);
		
		query.setRows(20);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		
		System.out.println("总记录数："+ page.getTotalElements());
		
		List<TbItem> list = page.getContent();
		
		for(TbItem item: list) {
			System.out.println(item.getTitle()+" "+item.getBrand()+" "+item.getPrice());
		}
	}
	
	@Test
	public void testPageQueryMutil() {
		
		Query query = new SimpleQuery("*:*");
		
		Criteria criteria = new Criteria("item_title").contains("2");
		
		criteria = criteria.and("item_title").contains("5");
		
		query.addCriteria(criteria);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		
		
		List<TbItem> list = page.getContent();
		
		for(TbItem item: list) {
			System.out.println(item.getTitle()+" "+item.getPrice());
		}
	}
	
	
	
	@Test
	public void testDeleteAll() {
		
		Query query = new SimpleQuery("*:*");
		
		solrTemplate.delete(query);
		
		solrTemplate.commit();
	}
	
	@Test
	public void testKeywords() {
		Query query = new SimpleQuery("*:*");
		Map<String, Object> searchMap = new HashMap();
		searchMap.put("keywords", "华为");
		

		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		ScoredPage<TbItem>page = solrTemplate.queryForPage(query, TbItem.class);
		
		List<TbItem> list = page.getContent();
		
		for(TbItem item: list) {
			System.out.println(item.getTitle()+" "+item.getPrice());
		}
	}
}
