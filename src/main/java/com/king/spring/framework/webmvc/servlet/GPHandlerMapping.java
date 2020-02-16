package com.king.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/15 9:44 下午
 * @Version: 1.0
 */
public class GPHandlerMapping {

	/**
	 * 保存方法对应的实例
	 */
	private Object controller;
	/**
	 * 保存映射的方法
	 */
	private Method method;
	/**
	 * URL的正则匹配
	 */
	private Pattern pattern;

	public GPHandlerMapping(Pattern pattern, Object controller, Method method) {
		this.controller = controller;
		this.method = method;
		this.pattern = pattern;
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
