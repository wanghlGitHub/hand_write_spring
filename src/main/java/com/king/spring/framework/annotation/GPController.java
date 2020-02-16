package com.king.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义 controller 注解
 *
 * @author heliang.wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPController {
	String value() default "";
}
