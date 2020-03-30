package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 分页查询数据
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//手写分页
		/*
		 * int total=itemMapper.selectCount(null);//获取总记录数 int start=(page-1) *
		 * rows;//定义起始位值 List<Item> itemList=itemMapper.findItemByPage(start,rows);
		 * 
		 */
		//MP分页
		IPage<Item> iPage = new Page<>(page,rows);//MP提供的分页内部的查询方法
		QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> resultPage = itemMapper.selectPage(iPage, queryWrapper);
		int total = (int) resultPage.getTotal();
		List<Item> itemList = resultPage.getRecords();
		return new EasyUITable(total,itemList);
	}

	//新增商品
	@Override
	public void saveItem(Item item) {
		item.setStatus(1)
		    .setCreated(new Date())
		    .setUpdated(item.getCreated());//上架时间相同
		itemMapper.insert(item);
		    }

	//更新item
	@Override
	public void updateItem(Item item) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
	}

	//根据指定id修改上下架状态
	//entity:修改数据的值  
	@Override
	public void updateStatus(Long[] ids, Integer status) {

		Item item = new Item();
		item.setStatus(status)
		    .setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		//数组转集合
		List<Long> idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
	}

	//删除商品信息
	@Override
	@Transactional
	public void deleteItem(Long[] ids) {
		//数组转集合
		List<Long> idsList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idsList);
		itemDescMapper.deleteBatchIds(idsList);
	}

	//重构添加商品信息
	@Override
	@Transactional//控制事务
	public void saveItem(Item item, ItemDesc itemDesc) {
		
		item.setStatus(1)
	    .setCreated(new Date())
	    .setUpdated(item.getCreated());//上架时间相同
	    itemMapper.insert(item);
	    
	    //MP操作时,当item入库后会将主键自动回显(item主键自增)
	    itemDesc.setItemId(item.getId())
	            .setCreated(item.getCreated())
	            .setUpdated(item.getCreated());
	    itemDescMapper.insert(itemDesc);
	}

	//查询商品详情
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	//修改商品详情
	@Override
	@Transactional
	public void updateItem(Item item, ItemDesc itemDesc) {

		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId())
		        .setUpdated(itemDesc.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	public Item findItemById(Long itmeId) {
		return itemMapper.selectById(itmeId);
	}

	

	
	
	
	
	
	
	
}
