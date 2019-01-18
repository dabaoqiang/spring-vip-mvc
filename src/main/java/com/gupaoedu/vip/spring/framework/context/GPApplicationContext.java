package com.gupaoedu.vip.spring.framework.context;

import com.gupaoedu.vip.spring.framework.annotation.GPAutowired;
import com.gupaoedu.vip.spring.framework.annotation.GPController;
import com.gupaoedu.vip.spring.framework.annotation.GPService;
import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;
import com.gupaoedu.vip.spring.framework.beans.GPBeanWrapper;
import com.gupaoedu.vip.spring.framework.context.support.GPBeanDefinitionReader;
import com.gupaoedu.vip.spring.framework.core.GPBeanFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author xiaoqiang
 * @Title: GPApplicationContext
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:29
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    // 配置文件 configLocation
    private String[] configLocations;

    // 既然需要定位资源，加载资源，因此，注入GPBeanDefinitionReader
    private GPBeanDefinitionReader gPBeanDefinitionReader;
    /**
     * getbean方法的initationBean方法保证bean的唯一
     */
    private Map<String, Object> beanCacheMap = new HashMap<String, Object>();
    /**
     * bean的封装类的封装
     */
    private Map<String, GPBeanWrapper> beanWrapperMap = new HashMap<String, GPBeanWrapper>();

    // 初始化容器
    public GPApplicationContext(String[] configLocations) {
        // 定位
        // 加载,两步，可以通过BeanDefinitionReader类去处理，专人干专事；
        this.gPBeanDefinitionReader = new GPBeanDefinitionReader(configLocations);
        // 注册 循环那个list，去添加到BeanDefinitionMap存储
        List<String> beanDefinitions = gPBeanDefinitionReader.loadBeanDefinitions();
        this.register(beanDefinitions);
        // 注入 循环map，进行BeanDefiniionMap的扫描；2，调用getBean获取真正的bean，3.调用poplulate
        // 进行真正的注入。
        autowried();
    }

    private void register(List<String> beanDefinitions) {
        //beanName有三种情况:
        //1、默认是类名首字母小写
        //2、自定义名字
        //3、接口注入
        try {
            for (String className : beanDefinitions) {
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，是不能实例化的
                //用它实现类来实例化
                if (beanClass.isInterface()) {
                    continue;
                }
                GPBeanDefinition beanDefinition = this.gPBeanDefinitionReader.registerBean(className);
                if (beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    this.beanDefinitionMap.put(i.getName(), beanDefinition);
                }
                //到这里为止，容器初始化完毕
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autowried() {
        // 循环加载资源
        for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                Object obj = getBean(beanName);
            }
        }

        // 循环注入

        for (Map.Entry<String, GPBeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()) {
            // 注入bean的真正方法是 populateBean
            populateBean(beanWrapperEntry.getKey(), beanWrapperEntry.getValue().getOriginalInstance());

        }


    }

    /**
     * 核心思路：
     * <p>
     * 第一，获取字节码，第二，判断是否是有controller，service
     * 第三，获取controller，service下面的注解的名称
     * 第四，调用反射set属性
     *
     * @param beanName
     * @param currentInstance
     */
    private void populateBean(String beanName, Object currentInstance) {
        Class<?> clazz = currentInstance.getClass();
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);

            try {
                field.set(currentInstance, this.beanWrapperMap.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }


    // 真正获取bean的方法
    // 1.获取bean的实例，需要判断，是否是存在，没有的话，新增数组实例
    // 2.如果是接口的话，就直接判断名字是否有，在一定条件下，是可以根据接口的实现类来查询到的bean
    // 3.instantionbean 实例化bean 专人做专是，在拆分
    public Object getBean(String beanName) {
        GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();
        try {
            Object instance = instantionBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
            this.beanWrapperMap.put(beanName, beanWrapper);
            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrapperInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取bean进行拆分
     *
     * @param beanDefinition
     * @return
     */
    private Object instantionBean(GPBeanDefinition beanDefinition) {

        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            //因为根据Class才能确定一个类是否有实例
            if (this.beanCacheMap.containsKey(className)) {
                instance = this.beanCacheMap.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.beanCacheMap.put(className, instance);
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig(){
        return this.gPBeanDefinitionReader.getConfig();
    }
}
