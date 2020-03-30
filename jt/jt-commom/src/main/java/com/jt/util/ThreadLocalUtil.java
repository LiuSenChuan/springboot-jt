package com.jt.util;

import com.jt.pojo.User;

public class ThreadLocalUtil {

	private static ThreadLocal<User> thread = new ThreadLocal<>();
	   public static void setUser(User user) {
	      thread.set(user);
	   }

	   public static User getUser() {

	      return thread.get();
	   }

	   public static void remove() {
		 //删除threadLocal中的数据,防止内存泄漏
	      thread.remove(); //清空数据.
	   }
}
