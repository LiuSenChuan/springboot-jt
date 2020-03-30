package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.annotation.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Component//将对象交给spring容器管理
@Aspect    //自定义切面
public class CacheAOP {

	//切面 = 切入点表达式  + 通知
	/**
	 * 1.添加环绕通知   环绕通知必须有返回值
	 * 2.拦截自定义注解
	 * 需求:动态获取自定义注解中的参数
	 *  工作原理说明
	 *      定义注解的变量名称 cacheFind
	 *      通知参数的接受名称 cacheFind
	 *      除了匹配名称外还需要匹配类型
	 *      
	 * 注意:
	 * 1.环绕通知使用时,必须添加ProceedingJoinPoint
	 * 并且其中的参数jionPoint必须位于第一位
	 * 
	 * 缓存实现思路
	 *    准备key,1.动态生成key  2.用户指定的key  key是否有值
	 *    先查询缓存
	 *      如果没有数据,执行数据库操作,执行目标方法.将目标方法的返回值转化为json格式存到Redis中
	 *      如果有数据  动态获取缓存数据之后利用工具api转化为真实对象
	 *      
      */
	
	/*
	 * @Autowired private Jedis jedis; //Redis单机
	 */	
	/*
	 * @Autowired private ShardedJedis jedis;//Redis分片机制
	 */	
	//@Autowired 
	//@Qualifier("sentinelJedis")//指定bean的名称注入
	//private Jedis jedis;//从哨兵中获取jedis
	@Autowired
	private JedisCluster jedis;
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		
		String key = getKey(joinPoint,cacheFind);
		String value = jedis.get(key);
		Object object = null;
		try {
			if (StringUtils.isEmpty(value)) {
				//缓存中没有数据则查询数据库
				object = joinPoint.proceed();
				String json = ObjectMapperUtil.toJson(object);
				//判断是否需要设置超时时长,将数据存入Redis中
				if (cacheFind.secondes()>0) {
					int seconds = cacheFind.secondes();
					jedis.setex(key, seconds, json);
				}else {
					jedis.set(key, json);
				}
				System.out.println("AOP查询数据库");
			}else{
				//缓存中有数据走缓存操作
				Class<?> targetClass = getReturnType(joinPoint);
				object = ObjectMapperUtil.toObj(value, targetClass);
				System.out.println("AOP查询缓存");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		//将jedis链接关闭,用户随用随关
		//jedis.close();
		return object;
		
	}

	//动态获取返回值类型
	private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		return methodSignature.getReturnType();
	}

	//動態獲取key
	private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
		//调用方法获取key
				String key = cacheFind.key();
				if (StringUtils.isEmpty(key)) {
					//key=类名.方法名.:参数
					String className = joinPoint.getSignature().getDeclaringTypeName();
					String methodName = joinPoint.getSignature().getName();
					Object arg0 = joinPoint.getArgs()[0];
					key = className+"."+methodName+"::"+arg0;
				}
				return key;
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * //前置通知 拦截所有的service层方法 //定义公共的切入点表达式
	 * 
	 * @Pointcut("execution(* com.jt.service..*.*(..))") public void pointCut() {
	 * 
	 * }
	 * 
	 * @Before("pointCut()")//通知关联切入点表达式 public void before(JoinPoint joinPoint) {
	 * //获取类名 String className = joinPoint.getSignature().getDeclaringTypeName();
	 * String methodName = joinPoint.getSignature().getName();//获取方法名
	 * System.out.println(className); System.out.println(methodName); }
	 */
}
