package com.gupaoedu.vip.spring.framework.beans;

/**
 * @author xiaoqiang
 * @Title: GPBeanDefinition
 * @ProjectName spring-vip-mvc
 * @Description: 用来存储配置文件中的信息
 * 相当于保存在内存中的配置
 * @date 2019-01-17 23:22
 */
public class GPBeanDefinition {
    /**
     * 类的全定称类名
     */
    private String beanClassName;

    private boolean lazyInit = false;
    /**
     * bean的名称，一般是首字母小写
     */
    private String factoryBeanName;

    /**
     * setter/getter
     */

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
