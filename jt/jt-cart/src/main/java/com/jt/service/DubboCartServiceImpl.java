package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * 新增购物车时,首先根据itemID和userID查询购物车数据,如果查询结果为null
	 * 则表示为null,表示用户第一次新增该商品,直接如果,如果不为空,则更新商品数量
	 */
	@Override
	public void saveCart(Cart cart) {

		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
		            .eq("user_id", cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		
		if (cartDB == null) {
			cart.setCreated(new Date())
			    .setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			Cart cartTemp = new Cart();
			int num = cartDB.getNum() + cart.getNum();
			cartTemp.setId(cartDB.getId()).setNum(num).setUpdated(new Date());
			//cartDB.setNum(num).setCreated(new Date());
			cartMapper.updateById(cartTemp);
		}
		
	}

	@Override
	public void updateNum(Cart cart) {

		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getId())
		             .eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);
	}

	@Override
	public void deleteCart(Cart cart) {

		//把cart传入意思是对象不为null的属性充当where条件
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
	}
}
