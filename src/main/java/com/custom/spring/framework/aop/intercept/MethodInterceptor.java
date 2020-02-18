package com.custom.spring.framework.aop.intercept;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:35 下午
 */
public interface MethodInterceptor {

	Object invoke(MethodInvocation invocation) throws Throwable;
}
