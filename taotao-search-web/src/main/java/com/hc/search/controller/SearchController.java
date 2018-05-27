package com.hc.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hc.common.pojo.SearchResult;
import com.hc.search.service.SearchService;

@Controller
public class SearchController {
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	@Autowired
	private SearchService searchService;
	@RequestMapping("/search")
	public String search(@RequestParam("q")String queryString,
			@RequestParam(defaultValue="1")Integer page,Model model){
		try {
			queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			SearchResult result = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
			model.addAttribute("query",queryString);
			model.addAttribute("totalPages", result.getTotalPages());
			model.addAttribute("itemList", result.getItemSearch());
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "search";
	}
}
