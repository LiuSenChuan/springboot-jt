package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{

	@Select("select * from tb_item order by updated DESC limit #{start},#{rows}")
	List<Item> findItemByPage(int start, Integer rows);
	
}
