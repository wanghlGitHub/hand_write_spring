package com.custom.spring.framework.aop.aspect;

import com.custom.spring.framework.aop.intercept.MethodInterceptor;
import com.custom.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: 异常通知
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:49 下午
 * @Version: 1.0
 */
public class AfterThrowingAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {

	private String throwingName;

	public AfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
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
