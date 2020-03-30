package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

@SpringBootTest
public class TestJson {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Test
	public void testJson() throws JsonProcessingException {
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L)
		        .setItemDesc("转换json");
		//将对象转换为json
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//json转换为对象
		ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2);
	}
	
	@Test
public void testJson2() throws JsonProcessingException {
		
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L).setItemDesc("转换json");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L).setItemDesc("转换json");
		
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		//将对象转换为json
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		
		//json转换为对象
		List<ItemDesc> itemDesc = MAPPER.readValue(json, list.getClass());
		System.out.println(itemDesc2);
	}
}
