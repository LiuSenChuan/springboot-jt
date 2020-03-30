package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		return "单点登录";
	}
	
	/**
	 * 实现用户数据的校验,跨域访问
	 * URL:http://sso.jt.com/user/check/{param}/{type}
	 * 参数:callback
	 * 返回值:sysResult的vo对象
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
			                     @PathVariable Integer type,
			                      String callback) {
		boolean flag = userService.checkUser(param,type); 
		SysResult sysResult = SysResult.success(flag);
		System.out.println(callback);
		return new JSONPObject(callback, sysResult);
		/*JSONPObject jsonpObject =null;
		try {
			//业务调用返回true/false
			boolean flag = userService.checkUser(param,type); 
			SysResult sysResult = SysResult.success(flag);
			jsonpObject = new JSONPObject(callback, sysResult);
		} catch (Exception e) {
			SysResult sysResult = SysResult.fail();
			jsonpObject = new JSONPObject(callback, sysResult);
			e.printStackTrace();
		}
		return jsonpObject;
	
*/
		}
	/**
	 * 完成用户信息的跨域获取
	 * url:http://sso.jt.com/user/query/5f9f1d14-998f-447f-8c9b-0652a6a872a8
	 *          ?callback=jsonp1584451979324&_=1584451979638
	 *参数:ticket callback
	 *返回值:sysresult的vo对象_ticket
	 *
	 */
	//@RequestMapping("/query/{ticket}")
	@RequestMapping("/query/{ticket}")
	public JSONPObject  findUserByTicket(@PathVariable String ticket,String callback,HttpServletResponse response) {
		
		String userJson = jedisCluster.get(ticket);
		if (StringUtils.isEmpty(userJson)) {
			//缓存中没有数据,证明ticket信息有问题,cookie有误,应该删除cookie
			//如何删除cookie  设置时间为0
			CookieUtil.deleteCookie(response, "JT_TICKET", "jt.com", "/");
			return new JSONPObject(callback, SysResult.fail());
		}
		//返回userjson
		SysResult sysResult = SysResult.success(userJson);
		return new JSONPObject(callback, sysResult);
	}
}
