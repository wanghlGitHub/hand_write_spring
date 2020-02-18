package com.custom.spring.framework.aop;

/**
 * @program: hand_write_spring
 * @description: aop 动态代理的顶层接口设计
 * @Author: heliang.wang
 * @Date: 2020/2/17 12:57 下午
 */
public interface AopProxy {

	Object getProxy();

	Object getProxy(ClassLoader classLoader);
}
