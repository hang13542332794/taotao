package com.hc.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hc.item.pojo.Item;
import com.hc.pojo.TbItem;
import com.hc.pojo.TbItemDesc;
import com.hc.service.ItemService;
 
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId,Model model) {
		//取商品基本信息
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		//取商品详情
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		//把数据传回给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc); 
		//返回逻辑视图
		return "item";
	}
}
