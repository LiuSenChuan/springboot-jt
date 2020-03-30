package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;


@Service
public class ItemCatServerImpl implements ItemCatServer {

	@Autowired
	private ItemCatMapper itemCatMapper;
	//@Autowired
	private Jedis Jedis;

	/**
	 * 查询商品分类对象获取分类名称
	 */
	@CacheFind
	@Override
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		return itemCat.getName();
	}

	/**
	 * 商品分类的展现
	 */
	@Override
	@CacheFind
	public List<EasyUITree> findItemCatListByParentId(Long parentId) {
		//根据parentID查询数据库记录
		List<ItemCat> itemCatList = findItemCatList(parentId);
		//itemCatList转化为list<easyUITree>
		List<EasyUITree> treeList = new ArrayList<EasyUITree>(itemCatList.size());
        for (ItemCat itemCat : itemCatList) {
        	Long id = itemCat.getId();
        	String text = itemCat.getName();
        	String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree uiTree = new EasyUITree(id, text, state);
			treeList.add(uiTree);
		}
		return treeList;
	}

	//根据parentid查询商品分类信息
	private List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}

	//缓存操作
	@Override
	public List<EasyUITree> findItemCatListByCache(Long parentId) {
		String key = "com.jt.service.ItemCatServiceImpl.findItemCatByCache::"+parentId;
		String value = Jedis.get(key);
		if (StringUtils.isEmpty(value)) {
			//缓存中没有数据,查询数据库,将数据存入Redis中
		List<EasyUITree> treeList = findItemCatListByParentId(parentId);
		String json = ObjectMapperUtil.toJson(treeList);
		Jedis.set(key, json);
		return treeList;
		}else {
			//缓存中有数据	需要将json转化为对象
		 List<EasyUITree> treeList = ObjectMapperUtil.toObj(value, List.class);
		 return treeList;
		}
		
	}
}
