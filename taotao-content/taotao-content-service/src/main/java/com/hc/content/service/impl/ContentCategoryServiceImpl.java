package com.hc.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.common.pojo.EasyUITreeNode;
import com.hc.common.pojo.TaotaoResult;
import com.hc.content.service.ContentCategoryService;
import com.hc.mapper.TbContentCategoryMapper;
import com.hc.pojo.TbContentCategory;
import com.hc.pojo.TbContentCategoryExample;
import com.hc.pojo.TbContentCategoryExample.Criteria;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}
	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		//创建一个pojo对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		//补全对象的属性
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		//'状态。可选值:1(正常),2(删除)',
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//插入到数据库
		tbContentCategoryMapper.insert(tbContentCategory);
		//判断父节点的状态
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!category.getIsParent()){
			category.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(category);
		}
		//返回结果
		return TaotaoResult.ok(tbContentCategory);
	}

}
