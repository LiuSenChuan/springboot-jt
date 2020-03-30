package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree {

	private Long id; //树形结构  唯一
	private String text; //节点文本信息
	private String state; //节点状态信息 closed关闭open打开
}
