package com.custom.spring.framework.aop.aspect;

import com.custom.spring.framework.aop.intercept.MethodInterceptor;
import com.custom.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: aop 切面方法执行之前的拦截器类
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:27 下午
 * @Version: 1.0
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {


	private JoinPoint joinPoint;

	public MethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
	}

	private void before(Method method, Object[] args, Object target) throws Throwable {
		//传送了给织入参数
		//method.invoke(target);
		super.invokeAdviceMethod(this.joinPoint, null, null);

	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		//从被织入的代码中才能拿到，JoinPoint
		this.joinPoint = mi;
		before(mi.getMethod(), mi.getArguments(), mi.getThis());
		return mi.proceed();
	}
}
