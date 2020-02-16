package com.king.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义 service 注解
 *
 * @author heliang.wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService {
	String value() default "";
}
