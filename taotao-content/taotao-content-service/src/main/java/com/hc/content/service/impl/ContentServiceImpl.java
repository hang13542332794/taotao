package com.hc.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hc.common.pojo.TaotaoResult;
import com.hc.common.utils.JsonUtils;
import com.hc.mapper.TbContentMapper;
import com.hc.pojo.TbContent;
import com.hc.pojo.TbContentExample;
import com.hc.pojo.TbContentExample.Criteria;
import com.taotao.jedis.JedisClient;

@Service
public class ContentServiceImpl implements com.hc.content.service.ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	@Override
	public TaotaoResult addContent(TbContent tbContent) {
		// 补全pojo属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		// 插入数据
		tbContentMapper.insert(tbContent);
		//同步缓存
		//删掉对应的缓存信息
		jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentBycid(long cid) {
		// 先查询缓存
		//查询缓存不能影响业务逻辑所以要try一下
		try {
			String json = jedisClient.hget(INDEX_CONTENT, cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = tbContentMapper.selectByExample(example);
		// 把结果添加到缓存
		//添加缓存不能影响业务逻辑所以要try一下
		try {
			jedisClient.hset(INDEX_CONTENT, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

}
