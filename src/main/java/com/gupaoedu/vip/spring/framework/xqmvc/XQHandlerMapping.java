package com.gupaoedu.vip.spring.framework.xqmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author xiaoqiang
 * @Title: XQHandlerMapping
 * @ProjectName spring-vip-mvc
 * @Description: url与处理相关的controller映射
 * @date 2019-01-20 12:32
 */
public class XQHandlerMapping {
    /**
     * 执行的controller
     */
    private Object controller;
    /**
     * 执行的方法
     */
    private Method method;
    /**
     * 执行的url映射
     */
    private Pattern pattern;

    /**
     * setter/getter
     */

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
