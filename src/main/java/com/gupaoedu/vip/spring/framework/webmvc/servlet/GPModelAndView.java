package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import java.util.Map;

/**
 * @author xiaoqiang
 * @Title: GPModelAndView
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-18 00:24
 */
public class GPModelAndView {

    private String viewName;
    private Map<String,?> model;

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
