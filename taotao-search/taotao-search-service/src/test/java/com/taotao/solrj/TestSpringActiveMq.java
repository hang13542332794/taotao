package com.taotao.solrj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActiveMq {

	@Test
	public void testSpringActiveMq() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-activemq.xml");
		//等待
		System.in.read();
	}
}
