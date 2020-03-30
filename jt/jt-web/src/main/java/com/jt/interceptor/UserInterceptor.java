package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component
public class UserInterceptor implements HandlerInterceptor{

	/**
	 * 一般使用拦截器的主要的目的是为了控制页面请求的跳转
	 *1.preHandle  处理器执行之前进行拦截
     *  2.postHandle 处理器执行完成之后拦截
     *  3.afterCompletion 全部业务逻辑执行完成之后并且视图渲染之后拦截
	 */
	/**
	 * 控制用户登录
	 * Boolean  
	 *    false  拦截请求    一般与重定向关联
	 *    true  放行方形请求
	 */
	/**
	 * 拦截器业务分析
	 *     如果用户登录则放行
	 *     如果用户未登录则拦截
	 *     判断用户是否登录
	 *   1.检查用户是否有cookie信息
	 *   2.检查redis服务器是否有ticket信息
	 *   如果正常则放行
	 */
	@Autowired
	private JedisCluster jedisCluster;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//判断用户是否有cookie
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		if (!StringUtils.isEmpty(ticket)) {
			//2.表示ticket信息不为null 有值
			String userJson = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJson)) {
				//redis数据一切正常,利用request对象传递用户信息
				//
				User user = ObjectMapperUtil.toObj(userJson, User.class);
				request.setAttribute("JT_USER", user);
				ThreadLocalUtil.setUser(user);
				return true;//请求放行
			}else {
				//用户有ticket,但是redis中没有改数据.,cookie有问题
	            CookieUtil.deleteCookie(response,"JT_TICKET","jt.com", "/");
			}
		}
		response.sendRedirect("/user/login.html");
		return true;
	}
}
