package com.hc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/")
	public String showPage(){
		
		return "index";
	}
	@RequestMapping("/{page}")
	public String findPage(@PathVariable String page){
		
		return page;
	}
}
