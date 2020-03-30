package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIImage;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
		
	}
	
	/**
	 * 添加商品
	 * @param item
	 * @return
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
	}
	/**
	 * 修改商品信息
	 * @param item
	 * @return
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
		
	}
	/**
	 * 下架商品
	 * 接受为ids串.如果字符串以","分割,则springmvc可以自动
	 * 的拆分为数组,springmvc可以自动的实现类型的转换
	 * @param ids
	 * @return
	 */
	@RequestMapping("/instock")
	public SysResult instockItem(Long[] ids) {
		int status = 2;
		itemService.updateStatus(ids,status);
		return SysResult.success();
		
	}
	/**
	 * 上架商品
	 * 接受为ids串.如果字符串以","分割,则springmvc可以自动
	 * 的拆分为数组,springmvc可以自动的实现类型的转换
	 * @param ids
	 * @return
	 */
	@RequestMapping("/reshelf")
	public SysResult reshelfItem(Long[] ids) {
		int status = 1;
		itemService.updateStatus(ids,status);
		return SysResult.success();
		
	}
	
	/**
	 * 删除商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		itemService.deleteItem(ids);
		return SysResult.success();
		
	}
	
	/**
	 * url:item/query/item/desc/1474391970
	 * 修改商品详情
	 * @PathVariable用来获取请求UR中的动态参数,即映射 URL 中的占位符到目标方法的参数中
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
		
	}
	

}
