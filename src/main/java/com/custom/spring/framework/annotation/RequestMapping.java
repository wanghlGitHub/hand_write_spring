package com.custom.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义 requestmapping 注解
 *
 * @author heliang.wang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
	String value() default "";
}
