package com.king.spring.framework.core;

/**
 * @program: hand_write_spring
 * @description: Spring 核心接口  beanFactory
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:15 下午
 */
public interface GPBeanFactory {

	/**
	 * 根据beanName从IOC容器中获得一个实例Bean
	 *
	 * @param beanName
	 * @return
	 */
	Object getBean(String beanName) throws Exception;

	/**
	 * 根据类型从 IOC 容器中获取 bean
	 *
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	Object getBean(Class<?> beanClass) throws Exception;
}
