package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(check = false)
	private DubboCartService cartService;
	/**
	 * 实现购物车的页面跳转
	 * URL:http://www.jt.com/cart/show.html
	 * 页面取值:${cartList}
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		//查询购物车页面信息
		//1.动态获取userID
		Long userId = ThreadLocalUtil.getUser().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		//2.jt-cart后台项目动态获取购物车记录
		//3.将数据展现在页面中
		return "cart";
	}
	/**
	 * 完成购物车的新增
	 * URL:http://www.jt.com/cart/add/562379.html
	 * 参数:form表单表达提交
	 * 返回值:页面逻辑名称 重定向到购物车页面
	 * 参数接受:如果参数名称与属性相同则可以直接转化
	 */
	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart) {
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}
	/**
	 * 实现购物车数量的更新
	 * url:http://www.jt.com/cart/update/num/562379/6
	 * 参数:itemId num
	 * 返回:sysresult
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setId(userId);
		cartService.updateNum(cart);
		return SysResult.success();
	}
	
	/**
	 * 实现购物车的删除,重定向到购物车页面
	 * URL:http://www.jt.com/cart/delete/562379.html
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
}
