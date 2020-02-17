package com.king.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:30 下午
 * @Version: 1.0
 */
public abstract class GPAbstractAspectAdvice implements GPAdvice {

	private Method aspectMethod;
	private Object aspectTarget;

	public GPAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
		this.aspectMethod = aspectMethod;
		this.aspectTarget = aspectTarget;
	}

	/**
	 * 通过反射机制，执行aop 切面类的具体方法
	 * @param joinPoint
	 * @param returnValue
	 * @param tx
	 * @return
	 * @throws Throwable
	 */
	public Object invokeAdviceMethod(GPJoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable {
		Class<?>[] paramTypes = this.aspectMethod.getParameterTypes();
		if (null == paramTypes || paramTypes.length == 0) {
			return this.aspectMethod.invoke(aspectTarget);
		} else {
			Object[] args = new Object[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) {
				if (paramTypes[i] == GPJoinPoint.class) {
					args[i] = joinPoint;
				} else if (paramTypes[i] == Throwable.class) {
					args[i] = tx;
				} else if (paramTypes[i] == Object.class) {
					args[i] = returnValue;
				}
			}
			return this.aspectMethod.invoke(aspectTarget, args);
		}
	}
}
