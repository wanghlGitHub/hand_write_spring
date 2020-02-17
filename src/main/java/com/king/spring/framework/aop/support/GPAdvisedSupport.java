package com.king.spring.framework.aop.support;

import com.king.spring.framework.aop.aspect.GPAfterReturningAdviceInterceptor;
import com.king.spring.framework.aop.aspect.GPAfterThrowingAdviceInterceptor;
import com.king.spring.framework.aop.aspect.GPMethodBeforeAdviceInterceptor;
import com.king.spring.framework.aop.config.GPAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: hand_write_spring
 * @description: aop 相关配置的包装类
 * @Author: heliang.wang
 * @Date: 2020/2/17 1:05 下午
 * @Version: 1.0
 */
public class GPAdvisedSupport {

	private Class<?> targetClass;

	private Object target;

	private GPAopConfig config;

	private Pattern pointCutClassPattern;

	private transient Map<Method, List<Object>> methodCache;

	public GPAdvisedSupport(GPAopConfig config) {
		this.config = config;
	}

	public Class<?> getTargetClass() {
		return this.targetClass;
	}

	public Object getTarget() {
		return this.target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
		parse();
	}

	private void parse() {
		String pointCut = config.getPointCut()
				.replaceAll("\\.", "\\\\.")
				.replaceAll("\\\\.\\*", ".*")
				.replaceAll("\\(", "\\\\(")
				.replaceAll("\\)", "\\\\)");
		//pointCut=public .* com.gupaoedu.vip.spring.demo.service..*Service..*(.*)
		//玩正则
		String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
		pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(
				pointCutForClassRegex.lastIndexOf(" ") + 1));

		try {
			methodCache = new HashMap<Method, List<Object>>();
			Pattern pattern = Pattern.compile(pointCut);
			//配置好的切面类
			Class aspectClass = Class.forName(this.config.getAspectClass());
			Map<String, Method> aspectMethods = new HashMap<String, Method>();
			//将切面类的所有方法缓存起来
			for (Method m : aspectClass.getMethods()) {
				aspectMethods.put(m.getName(), m);
			}

			for (Method m : this.targetClass.getMethods()) {
				String methodString = m.toString();
				if (methodString.contains("throws")) {
					methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
				}
				//匹配目标类里面的方法是否符合切面配置，如果正则匹配成功，创建对应的执行器链
				Matcher matcher = pattern.matcher(methodString);
				if (matcher.matches()) {
					//执行器链
					List<Object> advices = new LinkedList<Object>();
					//把每一个方法包装成 MethodIterceptor
					//before 前置通知
					if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))) {
						//创建一个Advivce
						advices.add(new GPMethodBeforeAdviceInterceptor(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
					}
					//after  后置通知
					if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))) {
						//创建一个Advivce
						advices.add(new GPAfterReturningAdviceInterceptor(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
					}
					//afterThrowing 异常通知
					if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow()))) {
						//创建一个Advivce
						GPAfterThrowingAdviceInterceptor throwingAdvice =
								new GPAfterThrowingAdviceInterceptor(aspectMethods.get(config.getAspectAfterThrow()), aspectClass.newInstance());
						throwingAdvice.setThrowName(config.getAspectAfterThrowingName());
						advices.add(throwingAdvice);
					}
					methodCache.put(m, advices);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否符合配置的 aop 切面
	 *
	 * @return
	 */
	public boolean pointCutMatch() {
		return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
	}

	/**
	 * 从已经缓存好的集合中，获取方法对应的执行器链
	 *
	 * @param method
	 * @param targetClass
	 * @return
	 * @throws Exception
	 */
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
		List<Object> cached = methodCache.get(method);
		if (cached == null) {
			Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
			cached = methodCache.get(m);
			//底层逻辑，对代理方法进行一个兼容处理
			this.methodCache.put(m, cached);
		}
		return cached;
	}
}
