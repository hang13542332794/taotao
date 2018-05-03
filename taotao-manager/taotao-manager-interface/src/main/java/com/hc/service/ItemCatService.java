package com.hc.service;

import java.util.List;

import com.hc.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCat(long parentId);
}
