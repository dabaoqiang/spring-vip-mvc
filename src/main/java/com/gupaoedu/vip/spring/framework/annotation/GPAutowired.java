package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: GPAutowired
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPAutowired {
    String value() default "";
}
