package com.custom.spring.framework.beans.config;

import lombok.Data;

/**
 * @program: hand_write_spring
 * @description: bean 内容的定义
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:23 下午
 * @Version: 1.0
 */
@Data
public class BeanDefinition {

	private String beanClassName;
	private boolean lazyInit = false;
	private String factoryBeanName;
	private boolean isSingleton = true;
}
