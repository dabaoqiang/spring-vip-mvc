package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author xiaoqiang
 * @Title: GPHandlerMapping
 * @ProjectName spring-vip-mvc
 * @Description: url对方法的封装
 * @date 2019-01-18 00:14
 */
public class GPHandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;  //url的封装

    public GPHandlerMapping(Pattern pattern,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
