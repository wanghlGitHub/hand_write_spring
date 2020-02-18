package com.custom.spring.framework.beans.support;

import com.custom.spring.framework.beans.config.BeanDefinition;
import com.custom.spring.framework.context.support.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hand_write_spring
 * @description: 存储所有的bean 实例
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:19 下午
 * @Version: 1.0
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {

	/**
	 * 存储注册信息的BeanDefinition
	 */
	protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
}
