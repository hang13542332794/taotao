package com.hc.service;

import com.hc.common.pojo.EasyUiDataGridResult;
import com.hc.common.pojo.TaotaoResult;
import com.hc.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(long itemId);
	EasyUiDataGridResult getItemList(int total,int rows);
	TaotaoResult addItem(TbItem item,String desc);
}
