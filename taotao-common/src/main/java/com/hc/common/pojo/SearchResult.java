package com.hc.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private long totalPages;//总页数
	private long totalrows;//总记录数
	private List<SearchItem> itemSearch;//商品列表
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemSearch() {
		return itemSearch;
	}
	public void setItemSearch(List<SearchItem> itemSearch) {
		this.itemSearch = itemSearch;
	}
	public long getTotalrows() {
		return totalrows;
	}
	public void setTotalrows(long totalrows) {
		this.totalrows = totalrows;
	}
	
}
