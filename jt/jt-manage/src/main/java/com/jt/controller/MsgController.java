package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {

	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/getMsg")
	public Integer getMsg() {
		
		return port;
	}
}
