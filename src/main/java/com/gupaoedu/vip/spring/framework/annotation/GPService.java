package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 业务逻辑,注入接口
 * @author xq
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService {
	String value() default "";
}
