package com.hc.serarch.mapper;

import java.util.List;

import com.hc.common.pojo.SearchItem;

public interface SearchItemMapper {
	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
