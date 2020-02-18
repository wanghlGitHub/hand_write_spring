package com.custom.spring.framework.test;

import com.custom.spring.demo.action.MyAction;
import com.custom.spring.framework.context.ApplicationContext;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/15 3:51 下午
 * @Version: 1.0
 */
public class TestSpringCase {
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ApplicationContext("application.properties");
		System.out.println("-------------");
		Object contextBean = applicationContext.getBean(MyAction.class);
		System.out.println("---------");
	}
}
