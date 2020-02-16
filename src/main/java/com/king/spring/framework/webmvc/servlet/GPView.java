package com.king.spring.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/16 12:14 下午
 * @Version: 1.0
 */
public class GPView {

	public final String DEFULAT_CONTENT_TYPE = "text/html;charset=utf-8";

	private File viewFile;

	public GPView(File viewFile) {
		this.viewFile = viewFile;
	}

	/**
	 * 视图渲染
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void render(Map<String, ?> model,
					   HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();

		RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");

		String line = null;
		while (null != (line = ra.readLine())) {
			line = new String(line.getBytes("ISO-8859-1"), "utf-8");
			Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				String paramName = matcher.group();
				paramName = paramName.replaceAll("￥\\{|\\}", "");
				Object paramValue = model.get(paramName);
				if (null == paramValue) {
					continue;
				}
				line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
				matcher = pattern.matcher(line);
			}
			sb.append(line);
		}

		response.setCharacterEncoding("utf-8");
//        response.setContentType(DEFULAT_CONTENT_TYPE);
		response.getWriter().write(sb.toString());
	}


	/**
	 * 处理特殊字符
	 *
	 * @param str
	 * @return
	 */
	public static String makeStringForRegExp(String str) {
		return str.replace("\\", "\\\\").replace("*", "\\*")
				.replace("+", "\\+").replace("|", "\\|")
				.replace("{", "\\{").replace("}", "\\}")
				.replace("(", "\\(").replace(")", "\\)")
				.replace("^", "\\^").replace("$", "\\$")
				.replace("[", "\\[").replace("]", "\\]")
				.replace("?", "\\?").replace(",", "\\,")
				.replace(".", "\\.").replace("&", "\\&");
	}
}
