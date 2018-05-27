package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisSpring {
	@Test
	public void redisSpring()throws Exception{
		//初始化spring容器
		ApplicationContext ApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-redis.xml");
		//从容器中获取JedisClient对象
		JedisClient jedisClient = ApplicationContext.getBean(JedisClient.class);
		jedisClient.set("jedisSpring", "cluster");
		String result = jedisClient.get("jedisSpring");
		System.out.println(result);
	}
}
