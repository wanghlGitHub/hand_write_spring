package com.custom.spring.framework.context;

/**
 * @program: hand_write_spring
 * @description: 自定义的 applicationContexyAware
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:27 下午
 * @Version: 1.0
 */
public interface ApplicationContextAware {

	void setApplicationContext(ApplicationContext applicationContext);
}
