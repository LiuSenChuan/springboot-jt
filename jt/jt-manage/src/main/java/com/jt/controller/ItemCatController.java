package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.service.ItemCatServer;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITree;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatServer ItemCatServer;
	@Autowired
	private ItemService itemServer;
	/**
	 * 获取商品分类名称
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		
		return ItemCatServer.findItemCatNameById(itemCatId);
	}
	/**
	 * 实现商品分类列表的展现
	 * defaultValue = "0"默认为父id(父id为0)
	 * @return
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findTreeByParentId(
			@RequestParam(value = "id",defaultValue = "0") Long parentId) {
		
		return ItemCatServer.findItemCatListByParentId(parentId);
		//return ItemCatServer.findItemCatListByCache(parentId);
	}
	

}
