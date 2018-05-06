package com.hc.content.service.impl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.common.pojo.TaotaoResult;
import com.hc.mapper.TbContentMapper;
import com.hc.pojo.TbContent;
@Service
public class ContentServiceImpl implements com.hc.content.service.ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	@Override
	public TaotaoResult addContent(TbContent tbContent) {
		//补全pojo属性
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		//插入数据
		tbContentMapper.insert(tbContent);
		return TaotaoResult.ok();
	}

}
