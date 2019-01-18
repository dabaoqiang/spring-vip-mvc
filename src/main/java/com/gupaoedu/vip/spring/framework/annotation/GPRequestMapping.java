package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 请求url
 * @author xq
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestMapping {
	String value() default "";
}
