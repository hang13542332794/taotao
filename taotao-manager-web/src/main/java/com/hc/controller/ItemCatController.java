package com.hc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.common.pojo.EasyUITreeNode;
import com.hc.service.ItemCatService;
@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCat(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> result = itemCatService.getItemCat(parentId);
		return result;
	}
}
