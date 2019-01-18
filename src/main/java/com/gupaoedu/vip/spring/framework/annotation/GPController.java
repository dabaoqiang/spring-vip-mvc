package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 页面交互
 * @author  xq
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPController {
    String value() default "";
}
