package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

	//定义mapper对象
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	//将对象转化为json
	public static String toJson(Object obj) {
		
		String result = null;
		try {
			result = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}
	
	//将json转化为对象
		public static <T> T toObj(String json,Class<T> targetClass) {
			System.out.println(json+"--------------");
			T t = null;
			try {
				t = MAPPER.readValue(json, targetClass);
				System.out.println(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			return t;
		}
}
