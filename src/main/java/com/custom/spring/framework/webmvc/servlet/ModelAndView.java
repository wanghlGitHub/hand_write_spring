package com.custom.spring.framework.webmvc.servlet;

import java.util.Map;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/16 12:06 下午
 * @Version: 1.0
 */
public class ModelAndView {

	private String viewName;
	private Map<String, ?> model;

	public ModelAndView(String viewName) {
		this.viewName = viewName;
	}

	public ModelAndView(String viewName, Map<String, ?> model) {
		this.viewName = viewName;
		this.model = model;
	}

	public String getViewName() {
		return viewName;
	}

	public Map<String, ?> getModel() {
		return model;
	}
}
