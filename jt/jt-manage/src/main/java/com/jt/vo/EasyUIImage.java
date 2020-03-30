package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EasyUIImage {

	private Integer error;
	private String url;
	private Integer width;
	private Integer height;
	
	//失败方法
	public static EasyUIImage fail() {
		
		return new EasyUIImage(1, null, null, null);
	}
	//成功方法
	public static EasyUIImage success(String url,Integer width,Integer height) {
			
		return new EasyUIImage(0, url, width, height);
		
	}
}
