package com.king.spring.framework.beans.support;

import com.king.spring.framework.beans.config.GPBeanDefinition;
import com.king.spring.framework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hand_write_spring
 * @description: 存储所有的bean 实例
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:19 下午
 * @Version: 1.0
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

	/**
	 * 存储注册信息的BeanDefinition
	 */
	protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, GPBeanDefinition>();
}
