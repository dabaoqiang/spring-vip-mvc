package com.gupaoedu.vip.spring.framework.core;

/**
 * @author xiaoqiang
 * @Title: GPBeanFactory
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:29
 */
public interface GPBeanFactory {

    /**
     * 根据beanName从IOC容器之中获得一个实例Bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
