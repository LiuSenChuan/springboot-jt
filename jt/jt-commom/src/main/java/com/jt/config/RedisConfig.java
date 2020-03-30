package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration // 标识配置类
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

	/*
	 * @Value("${redis.node}") private String redisNode;
	 * 
	 * @Bean //标识实例化对象类型
	 * 
	 * @Scope("prototype") //对象的多例 //reids单机 public Jedis jedis() { String host =
	 * redisNode.split(":")[0]; int port =
	 * Integer.parseInt(redisNode.split(":")[1]); return new Jedis(host, port); }
	 * 
	 * @Value("${redis.shards}") private String reidsShards;
	 * 
	 * @Bean
	 * 
	 * @Scope("prototype") //Redis分片机制 public ShardedJedis shardedJedis() { String[]
	 * nodeArray = reidsShards.split(","); List<JedisShardInfo> shards = new
	 * ArrayList<JedisShardInfo>(); for (String node : nodeArray) { String host =
	 * node.split(":")[0]; int port = Integer.parseInt(node.split(":")[1]);
	 * //每次循环添加一个node对象到list集合中 shards.add(new JedisShardInfo(host, port)); } return
	 * new ShardedJedis(shards);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 *//**
		 * 整合Redis哨兵 创建哨兵的池对象
		 */
	/*
	 * @Value("${redis.sentinel}") private String redisSentinel;
	 * 
	 * @Bean public JedisSentinelPool jedisSentinelPool() { Set<String> sentinels =
	 * new HashSet<>(); sentinels.add(redisSentinel); return new
	 * JedisSentinelPool("mymaster", sentinels); }
	 * 
	 *//**
		 * 动态获取池中的jedis对象
		 * 
		 * @Bean注解工作时,如果发现方法有参数列表则会自动注入
		 * @Qualifier 利用名称,实现对象的动态赋值 @Bean(name="pool") //给bean 动态的起名.
		 *//*
			 * @Bean
			 * 
			 * @Scope("prototype") public Jedis sentinelJedis(JedisSentinelPool
			 * jedisSentinelPool) { //该jedis有高可用效果 return
			 * jedisSentinelPool.getResource();//获取池中的jedis对象
			 * 
			 * }
			 */

	/**
	 * springboot配置redis集群
	 */
	@Value("${redis.clusters}")
	private String jedisClusters;
	
	@Bean
	@Scope("prototype")
	public JedisCluster jedisCluster() {
		Set<HostAndPort> setNodes = new HashSet<HostAndPort>();
		String[] nodes = jedisClusters.split(",");
		for (String node : nodes) {
			String host = node.split(":")[0];
			Integer port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			setNodes.add(hostAndPort);
		}
		return new JedisCluster(setNodes);

	}

}
