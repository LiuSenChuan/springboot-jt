package com.jt.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;

@RestControllerAdvice
@ResponseBody
public class SysResultException {

	/**
	 * 如果后台服务器发生运行时异常.则执行异常方法
	 * 跨域请求中一定会携带callback参数
	 */
	@ExceptionHandler(RuntimeException.class)
	public Object sysResult(Exception exception,HttpServletRequest request) {
		exception.printStackTrace(); //输出/log日志保存
		//如果用户传参中有callback参数,则执行跨域的异常返回
		String callback = request.getParameter("callback");
		if (StringUtils.isEmpty(callback)) {
			return SysResult.fail();
		}else {
			return new JSONPObject(callback, SysResult.fail());	
		}
		
		
	}
}
