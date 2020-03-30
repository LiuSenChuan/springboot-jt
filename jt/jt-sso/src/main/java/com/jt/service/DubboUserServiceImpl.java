package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	//用户入库时,必须传递email信息,否则程序报错  暂时用电话号码代替
	//用户加密:md5加密
	@Override
	public void saveUser(User user) {
        String md5PassWord = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5PassWord)
		    .setEmail(user.getPhone())
		    .setCreated(new Date())
		    .setUpdated(user.getCreated());
		userMapper.insert(user);
	}

	//完成用户数据的校验
	@Override
	public String findUserByUp(User user) {
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		//根据对象不为null的属性充当where条件ueryWrapper<User>(user);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		if (userDB==null) {
			return null;
		}
		//用户数据有效,需要进行单点登录
		String ticket = UUID.randomUUID().toString();
		//脱敏处理  伪造数据  防止盗密
		userDB.setPassword("865365你信么????");//为造数据
		String userJSON = ObjectMapperUtil.toJson(userDB);
		jedisCluster.setex(ticket, 7*24*3600, userJSON);
		
		return ticket;
	}

	
}
