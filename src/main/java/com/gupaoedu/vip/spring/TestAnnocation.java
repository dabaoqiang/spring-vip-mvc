package com.gupaoedu.vip.spring;

import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: TestAnnocation
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-20 14:21
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestAnnocation {

    public String pat() default "3";

    public String value() default "0";
}
