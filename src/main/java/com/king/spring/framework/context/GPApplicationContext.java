package com.king.spring.framework.context;

import com.king.spring.framework.annotation.GPAutowired;
import com.king.spring.framework.annotation.GPController;
import com.king.spring.framework.annotation.GPService;
import com.king.spring.framework.aop.GPAopProxy;
import com.king.spring.framework.aop.GPCglibAopProxy;
import com.king.spring.framework.aop.GPJdkDynamicAopProxy;
import com.king.spring.framework.aop.config.GPAopConfig;
import com.king.spring.framework.aop.support.GPAdvisedSupport;
import com.king.spring.framework.beans.GPBeanWrapper;
import com.king.spring.framework.beans.config.GPBeanDefinition;
import com.king.spring.framework.beans.config.GPBeanPostProcessor;
import com.king.spring.framework.beans.support.GPBeanDefinitionReader;
import com.king.spring.framework.beans.support.GPDefaultListableBeanFactory;
import com.king.spring.framework.core.GPBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: hand_write_spring
 * @description:
 * @Author: heliang.wang
 * @Date: 2020/2/15 2:29 下午
 * @Version: 1.0
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

	/**
	 * 配置文件路径
	 */
	private String[] configLoactions;
	private GPBeanDefinitionReader reader;

	/**
	 * 单例的IOC容器缓存
	 */
	private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
	/**
	 * 通用的IOC容器
	 */
	private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, GPBeanWrapper>();

	public GPApplicationContext(String... configLoactions) {
		this.configLoactions = configLoactions;
		try {
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 依赖注入，从这里开始，通过读取BeanDefinition中的信息
	 * 然后，通过反射机制创建一个实例并返回
	 * Spring做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
	 * 装饰器模式：
	 * 1、保留原来的OOP关系
	 * 2、我需要对它进行扩展，增强（为了以后AOP打基础）
	 *
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getBean(String beanName) throws Exception {

		GPBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
		Object instance = null;

		//这个逻辑还不严谨，自己可以去参考Spring源码
		//工厂模式 + 策略模式
		GPBeanPostProcessor postProcessor = new GPBeanPostProcessor();
		postProcessor.postProcessBeforeInitialization(instance, beanName);
		instance = instantiateBean(beanName, gpBeanDefinition);

		//3、把这个对象封装到BeanWrapper中
		GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);

		//4、把BeanWrapper存到IOC容器里面
		//1、初始化 -> 解决循环依赖的问题：先初始化所有的类，将其放到缓存中，然后再进行注入赋值，注入时，遇到循环依赖的情况，如果是单例的，直接获取，如果非单例，可以直接从缓存中直接通过无参构造方法进行new 获取
		//class A{ B b;}
		//class B{ A a;}
		//先有鸡还是先有蛋的问题，一个方法是搞不定的，要分两次

		//2、拿到BeanWraoper之后，把BeanWrapper保存到IOC容器中去
		this.factoryBeanInstanceCache.put(beanName, beanWrapper);
		//bean 初始化的后置处理，可根据具体的业务进行相关操作
		postProcessor.postProcessAfterInitialization(instance, beanName);

		//3、注入
		populateBean(beanName, new GPBeanDefinition(), beanWrapper);
		return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
	}

	/**
	 * DI注入
	 *
	 * @param beanName
	 * @param gpBeanDefinition
	 * @param gpBeanWrapper
	 */
	private void populateBean(String beanName, GPBeanDefinition gpBeanDefinition, GPBeanWrapper gpBeanWrapper) {
		Object instance = gpBeanWrapper.getWrappedInstance();
//        gpBeanDefinition.getBeanClassName();
		Class<?> clazz = gpBeanWrapper.getWrappedClass();
		//判断只有加了注解的类，才执行依赖注入
		if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
			return;
		}

		//获得所有的fields
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(GPAutowired.class)) {
				continue;
			}

			GPAutowired autowired = field.getAnnotation(GPAutowired.class);

			String autowiredBeanName = autowired.value().trim();
			if ("".equals(autowiredBeanName)) {
				autowiredBeanName = field.getType().getName();
			}

			//强制访问
			field.setAccessible(true);

			try {
				//为什么会为NULL，先留个坑,经过排查后发现，实例化时需要对所有加了注解的类进行排序处理
				//controller 中依赖了两个 service，如果在注入的时候，只实例化了一个，那么另外一个目前仍为空，需要保证所有的依赖全部实例化后才可以进行注入
				if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
					continue;
				}
				field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 实例化 bean
	 *
	 * @param beanName
	 * @param beanDefinition
	 * @return
	 */
	private Object instantiateBean(String beanName, GPBeanDefinition beanDefinition) {
		//1、拿到要实例化的对象的类名
		String className = beanDefinition.getBeanClassName();

		//2、反射实例化，得到一个对象
		Object instance = null;
		try {
//            gpBeanDefinition.getFactoryBeanName()
			//假设默认就是单例,细节暂且不考虑，先把主线拉通
			if (this.singletonObjects.containsKey(className)) {
				instance = this.singletonObjects.get(className);
			} else {
				Class<?> clazz = Class.forName(className);
				instance = clazz.newInstance();

				//实例化配置文件中的 aop 相关配置
				GPAdvisedSupport config = instantionAopConfig(beanDefinition);
				config.setTargetClass(clazz);
				config.setTarget(instance);

				//符合PointCut的规则的话，创建代理对象
				if(config.pointCutMatch()) {
					instance = createProxy(config).getProxy();
				}
				this.singletonObjects.put(className, instance);
				this.singletonObjects.put(beanDefinition.getFactoryBeanName(), instance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return instance;
	}

	/**
	 * 创建aop 动态代理对象
	 * @param config
	 * @return
	 */
	private GPAopProxy createProxy(GPAdvisedSupport config) {
		Class targetClass = config.getTargetClass();
		//如果存在接口数量，则使用 JDK 动态代理，否则使用 cglib 代理
		if(targetClass.getInterfaces().length > 0){
			return new GPJdkDynamicAopProxy(config);
		}
		return new GPCglibAopProxy(config);
	}

	/**
	 * 实例化 aop 相关配置
	 * @param beanDefinition
	 * @return
	 */
	private GPAdvisedSupport instantionAopConfig(GPBeanDefinition beanDefinition) {
		GPAopConfig config = new GPAopConfig();
		config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
		config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
		config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
		config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
		config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
		config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
		return new GPAdvisedSupport(config);
	}

	@Override
	public Object getBean(Class<?> beanClass) throws Exception {
		return getBean(beanClass.getName());
	}

	/**
	 * IOC容器启动入口
	 *
	 * @throws Exception
	 */
	@Override
	public void refresh() throws Exception {
		//1、定位
		reader = new GPBeanDefinitionReader(this.configLoactions);
		//2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition
		List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
		//3、注册,把配置信息放到容器里面(伪IOC容器)
		doRegisterBeanDefinition(beanDefinitions);
		//4、将所有beanDefinitionMap中已经注册好的 bean 进行排序，按照依赖管理
		//5、把不是延时加载的类，提前初始化
		doAutowrited();
	}

	/**
	 * 处理非延时加载的情况
	 */
	private void doAutowrited() {
		//从map 中获取所有的已经保存的 bean进行判断实例化
		for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
			String beanName = beanDefinitionEntry.getKey();
			if (!beanDefinitionEntry.getValue().isLazyInit()) {
				try {
					getBean(beanName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把配置信息放到伪 IOC 容器中，一个 map，此方法运行完毕，IOC 容器初始化完毕
	 *
	 * @param beanDefinitions
	 * @throws Exception
	 */
	private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception {
		if (beanDefinitions.isEmpty()) {
			return;
		}
		for (GPBeanDefinition beanDefinition : beanDefinitions) {
			if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
				throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
			}
			super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
		}
	}

	/**
	 * 获取ioc 容器中所有的bean的名称
	 * @return
	 */
	public String[] getBeanDefinitionNames() {
		return this.beanDefinitionMap.keySet().toArray(new  String[this.beanDefinitionMap.size()]);
	}

	public int getBeanDefinitionCount(){
		return this.beanDefinitionMap.size();
	}

	public Properties getConfig(){
		return this.reader.getConfig();
	}
}
