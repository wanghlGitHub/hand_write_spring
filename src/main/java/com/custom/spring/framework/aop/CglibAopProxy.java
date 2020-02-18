package com.custom.spring.framework.aop;

import com.custom.spring.framework.aop.support.AdvisedSupport;

/**
 * @program: hand_write_spring
 * @description: aop 动态代理的实现类  cglib
 * @Author: heliang.wang
 * @Date: 2020/2/17 12:59 下午
 * @Version: 1.0
 */
public class CglibAopProxy implements AopProxy {

	public CglibAopProxy(AdvisedSupport config) {

	}

	@Override
	public Object getProxy() {
		return null;
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return null;
	}
}