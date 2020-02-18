package com.custom.spring.framework.aop.aspect;

import com.custom.spring.framework.aop.intercept.MethodInterceptor;
import com.custom.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: 后置通知
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:46 下午
 * @Version: 1.0
 */
public class AfterReturningAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {

	private JoinPoint joinPoint;

	public AfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object retVal = mi.proceed();
		this.joinPoint = mi;
		this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
		return retVal;
	}

	private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
		super.invokeAdviceMethod(this.joinPoint, retVal, null);
	}
}
