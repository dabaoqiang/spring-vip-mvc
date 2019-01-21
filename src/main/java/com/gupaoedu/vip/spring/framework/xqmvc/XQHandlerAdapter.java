package com.gupaoedu.vip.spring.framework.xqmvc;

import com.gupaoedu.vip.spring.framework.webmvc.servlet.GPModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @author xiaoqiang
 * @Title: XQHandlerAdapter
 * @ProjectName spring-vip-mvc
 * @Description: 方法参数存储map，以及执行方法
 * @date 2019-01-20 12:34
 */
public class XQHandlerAdapter {

    /**
     * 存储key为方法名称，value为索引
     */
    private Map<String, Integer> paramMapping;


    public XQHandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    /**
     * 执行的方法
     * request 参数获取方法的请求或者url
     * response 写出
     *
     * @param request
     * @param response
     * @param xqHandlerMapping
     * @return
     */
    public GPModelAndView handler(HttpServletRequest request, HttpServletResponse response, XQHandlerMapping xqHandlerMapping) throws Exception {
        // 第一步，获取method的形参
        // 返回此对象所表示的方法的参数类型  方法参数的字节码类型
        Class<?>[] parameterTypes = xqHandlerMapping.getMethod().getParameterTypes();
        // 第二步，获取请求的参数类型
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 第三步，构造参数
        Object[] paramValue = new Object[parameterTypes.length];
        // 第四步，循环keySet
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            // 第五步，参数列表没有这个name，过滤
            if (!this.paramMapping.containsKey(param.getKey())) {
                continue;
            }
            // 第六步，根据name获取索引
            Integer index = this.paramMapping.get(param.getKey());
            // 第七步，给构造函数索引赋值
            paramValue[index] = caseStringValue(value, parameterTypes[index]);
        }

        // 第二步，处理HttpRequest，参数
        if (this.paramMapping.containsKey(HttpServletRequest.class.getName())) {
            Integer reqIndex = this.paramMapping.get(HttpServletRequest.class.getName());
            paramValue[reqIndex] = request;
        }
        if (this.paramMapping.containsKey(HttpServletResponse.class.getName())) {
            Integer resIndex = this.paramMapping.get(HttpServletResponse.class.getName());
            paramValue[resIndex] = response;
        }
        // 执行
        Object invoke = xqHandlerMapping.getMethod().invoke(xqHandlerMapping.getController(), paramValue);

        // 返回结果，处理
        if (invoke == null) {
            return null;
        }
        // 判断返回处理类型
        Boolean isMoldAndView = xqHandlerMapping.getMethod().getReturnType() == GPModelAndView.class;
        if (isMoldAndView) {
            return (GPModelAndView) invoke;
        } else {
            return null;
        }

    }

    /**
     * 转换parameterType所对应的值
     *
     * @param value
     * @param clazz
     */
    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        } else {
            return null;
        }
    }


}
