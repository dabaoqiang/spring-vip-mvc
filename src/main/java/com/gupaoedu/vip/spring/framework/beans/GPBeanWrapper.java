package com.gupaoedu.vip.spring.framework.beans;

import com.gupaoedu.vip.spring.framework.core.GPFactoryBean;

/**
 * @author xiaoqiang
 * @Title: GPBeanWrapper
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:24
 */
public class GPBeanWrapper extends GPFactoryBean {


    private Object wrapperInstance;

    /**
     * 原始的通过反射new出来，要把包装起来，存下来
     */
    private Object originalInstance;

    public GPBeanWrapper(Object originalInstance) {
        this.wrapperInstance = originalInstance;
        this.originalInstance = originalInstance;
    }

    /**
     * setter/getter
     */

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }

    public void setOriginalInstance(Object originalInstance) {
        this.originalInstance = originalInstance;
    }
}
