package com.hc.content.service;

import java.util.List;

import com.hc.common.pojo.EasyUITreeNode;
import com.hc.common.pojo.TaotaoResult;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	TaotaoResult addContentCategory(Long parentId,String name);
}
