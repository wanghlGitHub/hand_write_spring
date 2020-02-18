package com.custom.spring.framework.aop;

import com.custom.spring.framework.aop.intercept.MethodInvocation;
import com.custom.spring.framework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @program: hand_write_spring
 * @description: aop 动态代理 jdk 实现
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:01 下午
 * @Version: 1.0
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

	private AdvisedSupport advised;

	public JdkDynamicAopProxy(AdvisedSupport config) {
		this.advised = config;
	}

	@Override
	public Object getProxy() {
		return getProxy(this.advised.getTargetClass().getClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//获得执行器链
		List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
		MethodInvocation invocation = new MethodInvocation(proxy, this.advised.getTarget(), method, args, this.advised.getTargetClass(), interceptorsAndDynamicMethodMatchers);
		return invocation.proceed();
	}
}
