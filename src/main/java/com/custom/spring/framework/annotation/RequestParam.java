package com.custom.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义 requestParam 注解
 *
 * @author heliang.wang
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

	String value() default "";

	boolean required() default true;

}
