package test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestValue {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@org.junit.Test
	public void setValue() {
		
		redisTemplate.boundValueOps("name").set("张三");
		
	}
	
	@org.junit.Test
	public void getValue() {
		
		String name = (String)redisTemplate.boundValueOps("name").get();
		
		System.out.println(name);
	}
	
	
}
