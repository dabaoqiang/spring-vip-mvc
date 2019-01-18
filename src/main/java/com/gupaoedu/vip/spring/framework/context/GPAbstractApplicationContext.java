package com.gupaoedu.vip.spring.framework.context;

import java.util.List;

/**
 * @author xiaoqiang
 * @Title: GPAbstractApplicationContext
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:30
 */
public abstract  class GPAbstractApplicationContext {


    //提供给子类重写
    protected void onRefresh(){
        // For subclasses: do nothing by default.
    }

    protected abstract void refreshBeanFactory();

}
