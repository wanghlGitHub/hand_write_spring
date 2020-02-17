package com.king.spring.framework.aop.config;

import lombok.Data;

/**
 * @program: hand_write_spring
 * @description: aop 配置内容的封装类
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:03 下午
 * @Version: 1.0
 */
@Data
public class GPAopConfig {

	private String pointCut;
	private String aspectBefore;
	private String aspectAfter;
	private String aspectClass;
	private String aspectAfterThrow;
	private String aspectAfterThrowingName;
}
