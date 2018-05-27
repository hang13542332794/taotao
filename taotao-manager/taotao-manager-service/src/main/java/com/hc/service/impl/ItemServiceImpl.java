package com.hc.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.loading.PrivateClassLoader;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.AjAttribute.PrivilegedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hc.common.pojo.EasyUiDataGridResult;
import com.hc.common.pojo.TaotaoResult;
import com.hc.common.utils.IDUtils;
import com.hc.common.utils.JsonUtils;
import com.hc.mapper.TbItemDescMapper;
import com.hc.mapper.TbItemMapper;
import com.hc.pojo.TbItem;
import com.hc.pojo.TbItemDesc;
import com.hc.pojo.TbItemExample;
import com.hc.service.ItemService;
import com.taotao.jedis.JedisClient;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="addItemtopic")
	private Destination destination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${EXPIRE}")
	private Integer EXPIRE;
	@Override
	public TbItem getItemById(long itemId) {
		//先查询缓存，没有在查数据库
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
			if(StringUtils.isNoneBlank(json)){
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//把查询结果添加到redis
		try {
			jedisClient.set(ITEM_INFO+":"+itemId+":BASE", JsonUtils.objectToJson(item));
		//设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//补全item的属性
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表的对应的pojo
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全pojo属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//向商品描述插入数据
		tbItemDescMapper.insert(tbItemDesc);
		//向activemq发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId+"");
				return message;
			}
		});
		//返回结果
		return TaotaoResult.ok();
	}
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//先查询缓存，没有在查数据库
		try {
			String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
			if(StringUtils.isNoneBlank(json)){
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			//把查询结果添加到缓存
			jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
