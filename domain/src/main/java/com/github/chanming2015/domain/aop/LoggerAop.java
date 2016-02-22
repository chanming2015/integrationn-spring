package com.github.chanming2015.domain.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.aop
 * FileName:LoggerAop.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午7:55:19
 * Description: AOP日志接口
 * Version:1.0.0
 */
public interface LoggerAop {
	
	/**
	 * 
	 * @author XuMaoSen
	 * Create Time:2015年8月13日 上午10:49:43
	 * Description 前置通知
	 * @param param
	 */
	void beforeAdvice(String param);
	/**
	 * 
	 * @author XuMaoSen
	 * Create Time:2015年8月13日 上午10:40:35
	 * Description 后置返回通知
	 * @param retVal
	 */
	void afterReturningAdvice(Object retVal);
	/**
	 * 
	 * @author XuMaoSen
	 * Create Time:2015年8月13日 上午10:41:06
	 * Description 后置异常通知
	 * @param exception
	 */
	void afterThrowingAdvice(Exception exception);
	/**
	 * 
	 * @author XuMaoSen
	 * Create Time:2015年8月13日 上午10:41:40
	 * Description 后置最终通知
	 */
	void afterFinallyAdvice();
	/**
	 * 
	 * @author XuMaoSen
	 * Create Time:2015年8月13日 上午10:42:51
	 * Description 环绕通知
	 * @param pjp
	 */
	Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable;

}
