package com.custom.spring.framework.beans;

/**
 * @program: hand_write_spring
 * @description: 真正的 IOC 容器，对 bean 进行了包装
 * @Author: heliang.wang
 * @Date: 2020/2/15 3:30 下午
 * @Version: 1.0
 */
public class BeanWrapper {

	private Object wrappedInstance;
	private Class<?> wrappedClass;

	public BeanWrapper(Object wrappedInstance) {
		this.wrappedInstance = wrappedInstance;
	}

	public Object getWrappedInstance() {
		return this.wrappedInstance;
	}

	// 返回代理以后的Class
	// 可能会是这个 $Proxy0
	public Class<?> getWrappedClass() {
		return this.wrappedInstance.getClass();
	}
}
