package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatServer {

	String findItemCatNameById(Long itemCatId);

	List<EasyUITree> findItemCatListByParentId(Long parentId);

	List<EasyUITree> findItemCatListByCache(Long parentId);

}
