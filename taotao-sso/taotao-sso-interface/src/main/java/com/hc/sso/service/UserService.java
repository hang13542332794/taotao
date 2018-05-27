package com.hc.sso.service;

import com.hc.common.pojo.TaotaoResult;
import com.hc.pojo.TbUser;

public interface UserService {
	TaotaoResult checkData(String data,int type);
	TaotaoResult register(TbUser user);
	TaotaoResult login(String username,String password);
	TaotaoResult getUserByToken(String token);
}
