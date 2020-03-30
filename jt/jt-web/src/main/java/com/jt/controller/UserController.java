package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//rpc调用   不要求必须先启动服务提供者
	@Reference(check = false)
	private DubboUserService userService;

	/**
	 * http://www.jt.com/user/login.html
	 * http://www.jt.com/user/register.html
	 * @return
	 */
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	/**
	 * 注册
	 * url:http://www.jt.com/user/doRegister
	 * 参数password: admin123  username: admin1  phone: 13214785421
	 * 返回值  SysResult
	 * @return
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		  userService.saveUser(user);
		return SysResult.success();
	}
	
	/**
	 * sso单点登录
	 * URL:http://www.jt.com/user/doLogin
	 * 参数:username: admin12   password: qwerty
	 * 返回值:sysresult
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult findUserByUp(User user,HttpServletResponse response) {
		String ticket = userService.findUserByUp(user);
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		//如果ticket信息里边有值,则将数据保存到cookie中
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setDomain("jt.com");//设置cookie的共享 在jt.com的域名种实现共享
		cookie.setPath("/"); //cookie的权限  一般写/  代表根下的所有
		cookie.setMaxAge(7*24*3600);//7天超时
		response.addCookie(cookie);
		return SysResult.success();
	}
	
	
	
}
