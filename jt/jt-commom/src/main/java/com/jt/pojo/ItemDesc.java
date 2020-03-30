package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_item_desc")
public  class ItemDesc extends BasePojo{

	@TableId
	private Long itemId;//不适用主键自增是因为要与商品表中的id一致
	private String itemDesc;
}
