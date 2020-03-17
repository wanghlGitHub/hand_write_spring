package com.custom.spring.framework.aop;

import com.custom.spring.framework.aop.support.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: aop 动态代理的实现类  cglib
 * @Author: heliang.wang
 * @Date: 2020/2/17 12:59 下午
 * @Version: 1.0
 */
public class CglibAopProxy implements AopProxy, MethodInterceptor {

	private Object target;

	public CglibAopProxy(AdvisedSupport config) {
		Class<?> clazz = config.getTargetClass();
		Enhancer e = new Enhancer();
		e.setSuperclass(clazz);
		e.setCallback(this);
		target = e.create();
	}

	/**
	 * 生成继承代理目标类的子类
	 * @param clazz
	 * @return
	 */
//	public Object getInstance(Class<?> clazz) {
//		Enhancer e = new Enhancer();
//		e.setSuperclass(clazz);
//		e.setCallback(this);
//		return e.create();
//	}

	@Override
	public Object getProxy() {
		return this.target;
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return null;
	}

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		//子类调用父类的方法
		Object invoke = methodProxy.invokeSuper(o, objects);
		return invoke;
	}
}
