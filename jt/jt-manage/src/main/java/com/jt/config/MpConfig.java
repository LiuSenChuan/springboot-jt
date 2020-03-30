package com.jt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
public class MpConfig {

	//MP分页拦截器
	    @Bean//实例化对象交给spring管理
	    public PaginationInterceptor paginationInterceptor() {
	       
	        return new PaginationInterceptor();
	    }
}
