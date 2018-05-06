package com.hc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hc.common.pojo.EasyUiDataGridResult;
import com.hc.common.pojo.TaotaoResult;
import com.hc.common.utils.IDUtils;
import com.hc.mapper.TbItemDescMapper;
import com.hc.mapper.TbItemMapper;
import com.hc.pojo.TbItem;
import com.hc.pojo.TbItemDesc;
import com.hc.pojo.TbItemExample;
import com.hc.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Override
	public TbItem getItemById(long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}
	@Override
	public EasyUiDataGridResult getItemList(int total, int rows) {
		//设置分页信息
		//使用PageHelper分页插件里的静态方法
		PageHelper.startPage(total, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUiDataGridResult result = new EasyUiDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		//生成商品id
		long id = IDUtils.genItemId();
		item.setId(id);
		//补全item的属性
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表的对应的pojo
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全pojo属性
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//向商品描述插入数据
		tbItemDescMapper.insert(tbItemDesc);
		//返回结果
		return TaotaoResult.ok();
	}

}
