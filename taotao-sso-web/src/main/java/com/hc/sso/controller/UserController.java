package com.hc.sso.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.common.pojo.TaotaoResult;
import com.hc.common.utils.CookieUtils;
import com.hc.common.utils.JsonUtils;
import com.hc.pojo.TbUser;
import com.hc.sso.service.UserService;

@Controller
public class UserController {
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Autowired
	private UserService userService;
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type){
		TaotaoResult result = userService.checkData(param, type);
		return result;
	}
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		TaotaoResult result = userService.register(user);
		return result;
	}
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,
			HttpServletRequest request,HttpServletResponse response){
		TaotaoResult result = userService.login(username, password);
		if(result.getStatus()==200){
			//把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
		}
		return result;
	}
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result = userService.getUserByToken(token);
		if(StringUtils.isNoneBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
