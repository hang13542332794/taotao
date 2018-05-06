package com.hc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.common.pojo.EasyUITreeNode;
import com.hc.common.pojo.TaotaoResult;
import com.hc.content.service.ContentCategoryService;

@Controller
public class ContentCatogoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatogoryList(@RequestParam(value="id",defaultValue="0")Long parentId){
		List<EasyUITreeNode> resultList = contentCategoryService.getContentCategoryList(parentId);
		return resultList;
	}
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(Long parentId,String name){
		TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
}
