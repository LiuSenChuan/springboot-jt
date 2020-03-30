package com.jt.test;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

@SpringBootTest
public class redisTest {

	@Test
	public void testString() {
		
		String host = "192.168.83.174";
		int port = 6379;
		Jedis jedis = new Jedis(host, port);
		String result = jedis.set("1910", "共抗疫情");
		System.out.println("结果为:"+result);
		String value = jedis.get("1910");
		System.out.println("获取的value为:"+value);
		
	}
	
	Jedis jedis = null;
	
	@BeforeEach
	public void test() {
		String host = "192.168.83.174";
		int port = 6379;
		jedis = new Jedis(host, port);
	}
	/**
	 * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 */
	@Test
	public void test02() {
		if (!jedis.exists("1910")) {
			jedis.set("1910", "测试是否存在");
		}
		System.out.println(jedis.get("1910"));
	}
	/**
	 * 如果Redis存在这个值,则不允许赋值
	 */
	@Test
	public void test03() {
		jedis.setnx("1910", "setnx测试");
		System.out.println(jedis.get("1910"));
	}
	
	/**
	 * 为key值设置超时时间
	 * 原子性操作 : 要求赋值和超时时间同时设置
	 * @throws InterruptedException
	 */
	@Test
	public void test04() throws InterruptedException {
		jedis.setnx("1910", "超时时间测试");
		jedis.expire("1910", 20);
		Thread.sleep(3000);
		Long second = jedis.ttl("1910");
		System.out.println("剩余存活时间"+second);
	}
	//测试原子性
	@Test
	public void test05(){
		jedis.setex("1910", 20, "setex超时时间测试");
		System.out.println(jedis.ttl("1910"));
	}
	
	/**
	 * 进行操作和添加时间同时完成,并且不允许修改已有的数据
	 * setnx 如果数据存在则不赋值
	 * setex 可是实现赋值和超时操作
	 *    XX = "xx";完整赋值不考虑之前是否存在
          NX = "nx";如果存在则不赋值
          PX = "px";超时时间单位毫秒
          EX = "ex";超时时间单位秒
	 */
	@Test
	public void test06(){
		SetParams params = new SetParams();
		params.nx().ex(60);
		jedis.set("1910", "sadadsada", params );
		System.out.println(jedis.ttl("1910"));
	}
	
	/**
	 * 操作hash数据类型,保存对象的数据
	 * 如果有一类数据需要Redis保存时,可以使用hash
	 */
	@Test
	public void testHash() {
		jedis.hset("user", "id", "11");
		jedis.hset("user", "name", "爱谁谁");
		jedis.hset("user", "age", "11");
		Map<String, String> mapJe = jedis.hgetAll("user");
		System.out.println(mapJe);
	}
	//测试Redis队列
	@Test
	public void testList() {
		jedis.lpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
	}
	
	@Test
	public void testTX() {
		Transaction transaction = jedis.multi();
		try {
			transaction.set("a", "a");
			transaction.set("b", "b");
			transaction.exec();
		} catch (Exception e) {
              e.printStackTrace();
              transaction.discard();
		}
	}
	
}
