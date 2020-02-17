package com.king.spring.framework.aop.aspect;

import com.king.spring.framework.aop.intercept.GPMethodInterceptor;
import com.king.spring.framework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: 异常通知
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:49 下午
 * @Version: 1.0
 */
public class GPAfterThrowingAdviceInterceptor extends GPAbstractAspectAdvice implements GPAdvice, GPMethodInterceptor {

	private String throwingName;

	public GPAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
	}

	@Override
	public Object invoke(GPMethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		} catch (Throwable e) {
			invokeAdviceMethod(mi, null, e.getCause());
			throw e;
		}
	}

	public void setThrowName(String throwName) {
		this.throwingName = throwName;
	}
}
