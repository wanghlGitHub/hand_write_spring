package com.king.spring.framework.aop.aspect;

import com.king.spring.framework.aop.intercept.GPMethodInterceptor;
import com.king.spring.framework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: aop 切面方法执行之前的拦截器类
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:27 下午
 * @Version: 1.0
 */
public class GPMethodAroundAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {


	private GPJoinPoint joinPoint;

	public GPMethodAroundAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
	}

	/**
	 * 环绕通知
	 * @param method
	 * @param args
	 * @param target
	 * @throws Throwable
	 */
	private void around(Method method, Object[] args, Object target) throws Throwable {
		super.invokeAdviceMethod(this.joinPoint, null, null);

	}

	@Override
	public Object invoke(GPMethodInvocation mi) throws Throwable {
		//从被织入的代码中才能拿到，JoinPoint
		this.joinPoint = mi;
		around(mi.getMethod(), mi.getArguments(), mi.getThis());
		Object returnVal = mi.proceed();
		around(mi.getMethod(), mi.getArguments(), mi.getThis());
		return returnVal;
	}
}
