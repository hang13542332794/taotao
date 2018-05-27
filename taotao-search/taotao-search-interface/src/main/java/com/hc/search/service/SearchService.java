package com.hc.search.service;

import com.hc.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String queryString,int page,int rows)throws Exception;
}
