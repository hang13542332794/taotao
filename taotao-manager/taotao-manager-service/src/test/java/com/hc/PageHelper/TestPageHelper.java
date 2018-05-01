package com.hc.PageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hc.mapper.TbItemMapper;
import com.hc.pojo.TbItem;
import com.hc.pojo.TbItemExample;

public class TestPageHelper {
	@Test
	public void testPageHelper()throws Exception{
		//1在mybatis配置文件中配置分页插件
		//2在执行查询之前配置分页插件，使用PageHelper的静态
		PageHelper.startPage(1, 10);
		//3执行查询
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-dao.xml");
		TbItemMapper bean = applicationContext.getBean(TbItemMapper.class);
		TbItemExample example = new TbItemExample();
		//Criteria criteria = example.createCriteria();
		List<TbItem> list = bean.selectByExample(example);
		//4取分页信息，使用PageInfo对象取
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println("总记录数："+pageInfo.getTotal());
		System.out.println("总页数："+pageInfo.getPages());
		System.out.println("返回的记录数："+list.size());
	}
}
