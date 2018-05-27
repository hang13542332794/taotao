package com.hc.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.hc.common.pojo.TaotaoResult;
import com.hc.common.utils.JsonUtils;
import com.hc.mapper.TbUserMapper;
import com.hc.pojo.TbUser;
import com.hc.pojo.TbUserExample;
import com.hc.pojo.TbUserExample.Criteria;
import com.hc.sso.service.UserService;
import com.taotao.jedis.JedisClient;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public TaotaoResult checkData(String data, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		// 判断用户名是否可用
		if (type == 1) {
			criteria.andUsernameEqualTo(data);
		}
		// 判断手机号是否可用
		else if (type == 2) {
			criteria.andPhoneEqualTo(data);
		}
		// 判断邮箱是否可用
		else if (type == 3) {
			criteria.andEmailEqualTo(data);
		} else {
			return TaotaoResult.build(400, "参数中包含非法数据");
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 查询出数据，返回false
			return TaotaoResult.ok(false);
		}
		// 没查询到数据，证明这个数据可用
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser user) {
		if (StringUtils.isNoneBlank(user.getUsername())) {
			TaotaoResult.build(400, "用户名不能为空");
		}
		if (StringUtils.isNoneBlank(user.getPassword())) {
			TaotaoResult.build(400, "密码不能为空");
		}
		// 校验用户名是否可用
		TaotaoResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			TaotaoResult.build(400, "用户名已重复");
		}
		// 校验电话是否可用
		if (StringUtils.isNoneBlank(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				TaotaoResult.build(400, "电话已重复");
			}
		}
		// 校验邮箱是否可用
		if (StringUtils.isNoneBlank(user.getEmail())) {
			result = checkData(user.getEmail(), 2);
			if (!(boolean) result.getData()) {
				TaotaoResult.build(400, "邮箱已重复");
			}
		}
		//补全其他属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码加密,spring自带的加密工具
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		//判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		TbUser user = list.get(0);
		//密码要进行md5加密然后再校验
		if (!DigestUtils.md5DigestAsHex(password.getBytes())
				.equals(user.getPassword())) {
			//返回登录失败
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		//生成token，使用uuid
		String token = UUID.randomUUID().toString();
		//清空密码
		user.setPassword(null);
		//把用户信息保存到redis，key就是token，value就是用户信息
		jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
		//设置key的过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		//返回登录成功，其中要把token返回。
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(USER_SESSION + ":" + token);
		if(StringUtils.isNoneBlank(json)){
			TaotaoResult.build(400, "用户登录已过期");
		}
		//重置过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}

}
