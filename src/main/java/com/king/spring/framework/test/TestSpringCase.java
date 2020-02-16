package com.king.spring.framework.test;

import com.king.spring.demo.action.MyAction;
import com.king.spring.framework.context.GPApplicationContext;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/15 3:51 下午
 * @Version: 1.0
 */
public class TestSpringCase {
	public static void main(String[] args) throws Exception {
		GPApplicationContext applicationContext = new GPApplicationContext("application.properties");
		System.out.println("-------------");
		Object contextBean = applicationContext.getBean(MyAction.class);
		System.out.println("---------");
	}
}
