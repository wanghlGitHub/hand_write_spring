package com.custom.spring.framework.webmvc.servlet;

import com.custom.spring.framework.context.ApplicationContext;
import com.custom.spring.framework.annotation.Controller;
import com.custom.spring.framework.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: hand_write_spring
 * @description: 自定义的MVC DispatchServlet
 * @Author: heliang.wang
 * @Date: 2020/2/15 9:28 下午
 * @Version: 1.0
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

	private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

	private ApplicationContext context;
	/**
	 * 保存 url 与 handlerMapping 的映射关系集合
	 */
	private List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();
	/**
	 * 保存每一个 handlerMapping 与其对应参数的映射关系
	 */
	private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<HandlerMapping, HandlerAdapter>();

	/**
	 * 存放所有的视图
	 */
	private List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.doDispatch(req, resp);
		} catch (Exception e) {
			resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
			e.printStackTrace();
            new ModelAndView("500");
		}
	}

	/**
	 * 具体处理业务的方法
	 *
	 * @param req
	 * @param resp
	 */
	private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		//1、通过从request中拿到URL，去匹配一个HandlerMapping
		HandlerMapping handler = getHandler(req);

		//请求的 url 不存在，返回 404 页面
		if (handler == null) {
			processDispatchResult(req, resp, new ModelAndView("404"));
			return;
		}

		//2、准备调用前的参数
		HandlerAdapter ha = getHandlerAdapter(handler);

		//3、真正的调用方法,返回ModelAndView存储了要穿页面上值，和页面模板的名称
		ModelAndView mv = ha.handle(req, resp, handler);

		//这一步才是真正的输出
		processDispatchResult(req, resp, mv);
	}

	/**
	 * 通过已知的 handler ，获取初始化时保存的映射好的 handlerAdapter
	 *
	 * @param handler
	 * @return
	 */
	private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
		if (this.handlerAdapters.isEmpty()) {
			return null;
		}
		HandlerAdapter ha = this.handlerAdapters.get(handler);
		if (ha.supports(handler)) {
			return ha;
		}
		return null;
	}

	/**
	 * 请求返回结果的处理
	 *
	 * @param req
	 * @param resp
	 * @param mv
	 */
	private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
		//把给我的ModleAndView变成一个HTML、OuputStream、json、freemark、veolcity
		//ContextType
		if (null == mv) {
			return;
		}

		//如果ModelAndView不为null，怎么办？
		if (this.viewResolvers.isEmpty()) {
			return;
		}

		//从初始化时保存的视图集合中进行匹配
		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(mv.getViewName(), null);
			view.render(mv.getModel(), req, resp);
			return;
		}
	}

	private HandlerMapping getHandler(HttpServletRequest req) throws Exception {
		if (this.handlerMappings.isEmpty()) {
			return null;
		}

		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replace(contextPath, "").replaceAll("/+", "/");

		for (HandlerMapping handler : this.handlerMappings) {
			try {
				Matcher matcher = handler.getPattern().matcher(url);
				//如果没有匹配上继续下一个匹配
				if (!matcher.matches()) {
					continue;
				}

				return handler;
			} catch (Exception e) {
				throw e;
			}
		}
		return null;
	}

	/**
	 * 初始化
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		//1、初始化ApplicationContext
		context = new ApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
		//2、初始化Spring MVC 九大组件
		initStrategies(context);
	}

	/**
	 * 容器启动，初始化springMVC 9 大组件
	 *
	 * @param context
	 */
	private void initStrategies(ApplicationContext context) {

		//多文件上传的组件
		initMultipartResolver(context);
		//初始化本地语言环境
		initLocaleResolver(context);
		//初始化模板处理器
		initThemeResolver(context);
		//handlerMapping，必须实现
		/**
		 * 初始化 ioc容器中的 controller URL 与 method 的关系映射，保存到集合中
		 */
		initHandlerMappings(context);
		//初始化参数适配器，必须实现
		/**
		 * 在这里进行 handlermapping 与 对应参数的关系映射，有多少个 handlerMapping 就存在多少个 handlerAdapter
		 */
		initHandlerAdapters(context);
		//初始化异常拦截器
		initHandlerExceptionResolvers(context);
		//初始化视图预处理器
		initRequestToViewNameTranslator(context);
		//初始化视图转换器，必须实现
		initViewResolvers(context);
		//参数缓存器
		initFlashMapManager(context);
	}

	/**
	 * 初始化IOC 容器中加载的所有带有 @Controller 注解的bean,
	 * 解析 URL 与对应的 handler 关系映射，保存在集合中
	 *
	 * @param context
	 */
	private void initHandlerMappings(ApplicationContext context) {
		String[] beanNames = context.getBeanDefinitionNames();
		try {
			for (String beanName : beanNames) {
				Object controller = context.getBean(beanName);
				Class<?> clazz = controller.getClass();
				//只判断包含 @Controller 注解的类
				if (!clazz.isAnnotationPresent(Controller.class)) {
					continue;
				}
				String baseUrl = "";
				//获取Controller的url配置
				if (clazz.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
					baseUrl = requestMapping.value();
				}
				//获取Method的url配置
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					//没有加RequestMapping注解的直接忽略
					if (!method.isAnnotationPresent(RequestMapping.class)) {
						continue;
					}
					//映射URL
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					//springMVC 请求路径的正则表达式匹配处理
					String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
					Pattern pattern = Pattern.compile(regex);

					this.handlerMappings.add(new HandlerMapping(pattern, controller, method));
					log.info("Mapped " + regex + "," + method);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHandlerAdapters(ApplicationContext context) {
		//把一个requet请求变成一个handler，参数都是字符串的，自动配到handler中的形参

		//可想而知，他要拿到HandlerMapping才能干活
		//就意味着，有几个HandlerMapping就有几个HandlerAdapter
		for (HandlerMapping handlerMapping : this.handlerMappings) {
			this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
		}
	}

	private void initViewResolvers(ApplicationContext context) {
		//拿到模板的存放目录
		String templateRoot = context.getConfig().getProperty("templateRoot");
		String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

		File templateRootDir = new File(templateRootPath);
		String[] templates = templateRootDir.list();
		for (int i = 0; i < templates.length; i++) {
			//这里主要是为了兼容多模板，所有模仿Spring用List保存
			//在我写的代码中简化了，其实只有需要一个模板就可以搞定
			//只是为了仿真，所有还是搞了个List
			this.viewResolvers.add(new ViewResolver(templateRoot));
		}
	}

	private void initHandlerExceptionResolvers(ApplicationContext context) {

	}

	private void initFlashMapManager(ApplicationContext context) {

	}

	private void initRequestToViewNameTranslator(ApplicationContext context) {

	}

	private void initThemeResolver(ApplicationContext context) {

	}

	private void initLocaleResolver(ApplicationContext context) {

	}

	private void initMultipartResolver(ApplicationContext context) {

	}
}
