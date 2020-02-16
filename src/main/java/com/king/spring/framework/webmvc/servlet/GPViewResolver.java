package com.king.spring.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * @program: hand_write_spring
 * @description: 视图
 * @Author: heliang.wang
 * @Date: 2020/2/16 12:13 下午
 * @Version: 1.0
 */
public class GPViewResolver {

	private final String DEFAULT_TEMPLATE_SUFFX = ".html";

	private File templateRootDir;
//    private String viewName;

	public GPViewResolver(String templateRoot) {
		String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
		templateRootDir = new File(templateRootPath);
	}

	public GPView resolveViewName(String viewName, Locale locale) throws Exception {
		if (null == viewName || "".equals(viewName.trim())) {
			return null;
		}
		viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
		File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
		return new GPView(templateFile);
	}

//    public String getViewName() {
//        return viewName;
//    }
}
