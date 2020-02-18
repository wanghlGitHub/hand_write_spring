package com.custom.spring.framework.annotation;

import java.lang.annotation.*;


/**
 * 自定义 autowired 注解
 *
 * @author heliang.wang
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
	String value() default "";
}
