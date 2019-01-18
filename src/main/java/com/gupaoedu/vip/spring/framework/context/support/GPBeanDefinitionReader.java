package com.gupaoedu.vip.spring.framework.context.support;

import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author xiaoqiang
 * @Title: GPBeanDefinitionReader
 * @ProjectName spring-vip-mvc
 * @Description: 用对配置文件进行查找，读取、解析
 * @date 2019-01-17 23:34
 */
public class GPBeanDefinitionReader {


    // 加载resource下的配置文件到内存中，需要一个容器
    private Properties config = new Properties();

    // doScanner，肯定是某个指定的Key下面的属性，约定大于配置
    private final String SCAN_PACKAGE = "scanPackage";

    // 保存一个list用来存储以及加载定位加载资源的class定称名数组
    private List<String> registyBeanClasses = new ArrayList<String>();

    // 开始解析，需要地址
    public GPBeanDefinitionReader(String... locations) {
        // 定位资源
        //在Spring中是通过Reader去查找和定位对不对
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));

        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 加载资源，读取配置文件中的指定文件生成需要配置文件的全定劲类名
        doScanner(config.getProperty(SCAN_PACKAGE));

    }

    private void doScanner(String packageName) {

        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));

        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                registyBeanClasses.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }

    }

    public Properties getConfig(){
        return this.config;
    }

    public List<String> loadBeanDefinitions(){
        return this.registyBeanClasses;
    }

    //每注册一个className，就返回一个BeanDefinition，我自己包装
    //只是为了对配置信息进行一个包装
    public GPBeanDefinition registerBean(String className){
        if(this.registyBeanClasses.contains(className)){
            GPBeanDefinition beanDefinition = new GPBeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".") + 1)));
            return beanDefinition;
        }

        return null;
    }
    // 首字母小写
    private String lowerFirstCase(String str) {
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
