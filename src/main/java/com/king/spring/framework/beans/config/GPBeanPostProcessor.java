package com.king.spring.framework.beans.config;

/**
 * @program: hand_write_spring
 * @description: 初始化 bean 时的处理器
 * @Author: heliang.wang
 * @Date: 2020/2/15 3:33 下午
 * @Version: 1.0
 */
public class GPBeanPostProcessor {

	/**
	 * 前置处理器
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}

	/**
	 * 后置处理器
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}
}
