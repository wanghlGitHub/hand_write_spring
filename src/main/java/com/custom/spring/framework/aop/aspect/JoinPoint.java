package com.custom.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description: 切入点
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:32 下午
 */
public interface JoinPoint {

	Object getThis();

	Object[] getArguments();

	Method getMethod();

	void setUserAttribute(String key, Object value);

	Object getUserAttribute(String key);
}
