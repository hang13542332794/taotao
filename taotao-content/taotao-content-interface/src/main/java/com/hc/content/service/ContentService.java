package com.hc.content.service;

import java.util.List;

import com.hc.common.pojo.TaotaoResult;
import com.hc.pojo.TbContent;

public interface ContentService {
	TaotaoResult addContent(TbContent tbContent);
	List<TbContent> getContentBycid(long cid);
}
