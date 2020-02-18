package com.custom.spring.framework.context.support;

/**
 * @program: hand_write_spring
 * @description: IOC 容器顶层 抽象类
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:20 下午
 * @Version: 1.0
 */
public abstract class AbstractApplicationContext {

	/**
	 * 提供给其子类进行实现
	 *
	 * @throws Exception
	 */
	public void refresh() throws Exception {
	}
}
