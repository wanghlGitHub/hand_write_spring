# hand_write_spring
自我学习之高仿版 spring



IOC

定位、加载、注册
applicationContext -> refresh()入口，通过getResource()定位到资源文件，然后通过render.loadBeanDefinitions()加载类的定义，最后，保存到伪IOC容器中BeanDefinition（只是类的定义，并没有进行实例化，所以称为伪 IOC容器）


DI

applicationContext -> getBean()入口，instantiateBean() 创建 BeanWrapper,将伪IOC容器中的 bean 包装成 BeanWrapper，通过 populateBean()依赖注入实例化，最后保存到真正的 IOC 容器中（使用Map缓存,分为两种：1、单例型的 2、随取随用型的）

AOP

applicationContext -> getBean -> instantiateBean() 实例化时，创建代理对象，通过AdvisedSupport 读取配置文件中的 aop 相关配置，然后解析配置针对每一个方法创建一个 MethodInterceptor chain,
通过判断被pointCut织入的类有没有接口来进行创建动态代理，如果存在接口，使用 jdk 动态代理创建，否则使用 cglib 创建；方法在执行时，执行的是代理类，jdk动态代理实现了InvocationHandler接口，会默认触发 invoke方法，
在触发 invoke 方法中，通过 MethodInvocation.proceed() 调用对应的之前创建好的执行器链 MethodsInterceptor chain,  MethodInterceptor.invoke():before\afterReturn\around\after throw

MVC

DispatchServlet -> init、doPost，通过集成了 HttpServlet,实现 init方法进行初始化，初始化 MVC 的 9 大组件，其中较为关键的为 initHandlerMapping(URL 与 Method 关系的映射，将其保存到缓存中List<HandlerMapping>),
initHandlerAdapter(参数与 handlerMapping的关系映射，保存在 Map<HandlerMapping,HandlerAdapter>中),initViewResolvers()视图解析器，通过读取配置的路径，将所有指定位置的页面缓存起来List<ViewResolver>;业务调用阶段，通过
doDispatch方法，解析 request 中的 URL拿到具体的 handlerMapping，拿到 handlerAdapter 解析参数，通过反射执行方法获得返回结果ModelAndView，最后通过解析得到 View,调用 View.render 向页面输出结果。
