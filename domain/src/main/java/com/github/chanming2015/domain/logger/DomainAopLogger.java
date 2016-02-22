package com.github.chanming2015.domain.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.github.chanming2015.domain.aop.DefaultAopLogger;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.logger
 * FileName:DomainAopLogger.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午7:54:30
 * Description: doMain层AOP-Logger
 * Version:1.0.0
 */
@Aspect
@Component
public class DomainAopLogger extends DefaultAopLogger{
	
	@Pointcut(value="execution(* com.github.chanming2015.domain.service.impl.*.*(..))")
	public void doMainPoint() {	}
	
	@Around(value="doMainPoint()")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		return super.aroundAdvice(pjp);
	}
	
}
