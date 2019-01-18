package com.gupaoedu.vip.spring.framework.context;

import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoqiang
 * @Title: GPDefaultListableBeanFactory
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:30
 */
public class GPDefaultListableBeanFactory extends  GPAbstractApplicationContext {

    // 这里定义一个Map进行存储内存配置信息
    //beanDefinitionMap用来保存配置信息
    protected Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,GPBeanDefinition>();

    // 交给子类去实现
    protected void onRefresh() {
    }

    @Override
    protected void refreshBeanFactory() {

    }
}
