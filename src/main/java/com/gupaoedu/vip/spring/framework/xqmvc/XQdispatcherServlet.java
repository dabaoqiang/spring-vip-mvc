package com.gupaoedu.vip.spring.framework.xqmvc;

import com.gupaoedu.vip.spring.framework.annotation.GPController;
import com.gupaoedu.vip.spring.framework.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.framework.annotation.GPRequestParam;
import com.gupaoedu.vip.spring.framework.context.GPApplicationContext;
import com.gupaoedu.vip.spring.framework.webmvc.servlet.GPModelAndView;
import com.gupaoedu.vip.spring.framework.webmvc.servlet.GPViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaoqiang
 * @Title: XQdispatcherServlet
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-20 13:44
 */
public class XQdispatcherServlet extends HttpServlet {

    private String location = "contextConfigLocation";

    GPApplicationContext gpApplicationContext;

    // 定义一个HandlerMapping存储起来
    List<XQHandlerMapping> handlerMappingList = new ArrayList<XQHandlerMapping>();
    // 定义存储handlerMapping 对应的HandlerAdapter的对应List
    Map<XQHandlerMapping, XQHandlerAdapter> handlerAdapterMap = new HashMap<XQHandlerMapping, XQHandlerAdapter>();
    // 视图解析的list存储
    List<GPViewResolver> viewResolvers = new ArrayList<GPViewResolver>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化IOC容器
        this.gpApplicationContext = new GPApplicationContext(new String[]{location});
        // 初始话九大组件
        initStrategies(gpApplicationContext);
    }

    private void initStrategies(GPApplicationContext gpApplicationContext) {
        // 初始化url映射
        initHandlerMapping(gpApplicationContext);
        // 初始化handlerAdapter
        initHandlerAdpter(gpApplicationContext);
        // 视图解析
        initViewResolvers(gpApplicationContext);
    }

    /**
     * 初始化容器
     *
     * @param gpApplicationContext
     */
    private void initViewResolvers(GPApplicationContext gpApplicationContext) {
        String templateRoot = gpApplicationContext.getConfig().getProperty("templateRoot");
        String filePath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File file = new File(filePath);
        if (file.listFiles() != null) {
            for (File listFile : file.listFiles()) {
                viewResolvers.add(new GPViewResolver(listFile.getName(), listFile));
            }
        }
    }

    /**
     * 初始化handlerAdapter
     *
     * @param gpApplicationContext
     */
    private void initHandlerAdpter(GPApplicationContext gpApplicationContext) {
        //1.for循环所有的东西
        if (this.handlerMappingList == null) {
            return;
        }
        for (XQHandlerMapping xqHandlerMapping : this.handlerMappingList) {
            //2.定义一个handlerAdapter的参数
            Annotation[][] parameterAnnotations = xqHandlerMapping.getMethod().getParameterAnnotations();
            // 定义一个参数存储列表
            Map<String, Integer> paramterMap = new HashMap<String, Integer>();
            // 循环方法注释的参数
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof GPRequestParam) {
                        GPRequestParam paramName = (GPRequestParam) annotation;
                        paramterMap.put(paramName.value(), i);
                    }
                }
            }
            // 获取请求参数的字节码类型
            Class<?>[] parameterTypes = xqHandlerMapping.getMethod().getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                    paramterMap.put(parameterType.getName(), i);
                }
            }
            handlerAdapterMap.put(xqHandlerMapping, new XQHandlerAdapter(paramterMap));
        }

    }


    /**
     * 将url一一对应
     * 将在类上注解了Controller的RequestMapping的，注解，以及方法上的注解，RequestMapping全部加载到内存当中
     *
     * @param gpApplicationContext
     */
    private void initHandlerMapping(GPApplicationContext gpApplicationContext) {
        // !.循环beanCLassNameList
        if (gpApplicationContext.getBeanDefinitionNames() == null) {
            return;
        }
        // 2.获取
        String[] beanDefinitionNames = gpApplicationContext.getBeanDefinitionNames();
        // 3.循环
        for (String beanDefinitionName : beanDefinitionNames) {
            Object controller = gpApplicationContext.getBean(beanDefinitionName);
            Class<?> clazz = controller.getClass();
            // 4.是否打印了controller注解
            boolean annotationPresent = clazz.isAnnotationPresent(GPController.class);
            if (!annotationPresent) {
                continue;
            }
            // 第二步是否打印了requestMapping
            String baseUrl = "";
            if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                GPRequestMapping annotation = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = annotation.value();
            }
            // 类已经扫描完成，再加载所有public的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                    continue;
                }
                GPRequestMapping annotation = method.getAnnotation(GPRequestMapping.class);
                String value = annotation.value();
                String regx = "/" + baseUrl + "/" + value.replaceAll("\\*", ".*").replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regx);
                XQHandlerMapping xqHandlerMapping = new XQHandlerMapping();
                xqHandlerMapping.setController(controller);
                xqHandlerMapping.setMethod(method);
                xqHandlerMapping.setPattern(pattern);
                this.handlerMappingList.add(xqHandlerMapping);
            }

        }

    }

    // 九大组件初始化，开始进行处理请求阶段


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    /**
     * 四步
     * 1.根据url获取handlerMapping
     * 2.根据handlerMapping获取HandlerAdapter
     * 3.执行HandlerAdapter执行方法，返回ModelAndView
     * 4.解析向页面写入
     *
     * @param req
     * @param resp
     */
    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1
        XQHandlerMapping handler = getHandler(req);
        if (handler == null) {
            return;
        }
        // 2
        XQHandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        // 3
        GPModelAndView gPModelAndView = handlerAdapter.handler(req, resp, handler);
        // 4
        processDispatchResult(resp, gPModelAndView);
    }

    /**
     * 结果集处理
     *
     * @param resp
     * @param gPModelAndView
     */
    private void processDispatchResult(HttpServletResponse resp, GPModelAndView gPModelAndView) throws Exception {
        if (gPModelAndView == null) {
            return;
        }
        if (this.viewResolvers == null) {
            return;
        }
        // 解析
        for (GPViewResolver viewResolver : viewResolvers) {
            if (!viewResolver.getViewName().equals(gPModelAndView.getViewName())) {
                continue;
            }
            String out = viewResolver.viewResolver(gPModelAndView);
            if (out != null) {
                resp.getWriter().write(out);
                break;
            }
        }

    }


    /**
     * 获取handlerAdapter
     *
     * @param handler
     */
    private XQHandlerAdapter getHandlerAdapter(XQHandlerMapping handler) {
        if (handlerAdapterMap == null) {
            return null;
        }
        return this.handlerAdapterMap.get(handler);
    }

    /**
     * 返回url所对应的handlerMapping
     *
     * @param req
     * @return
     */
    private XQHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappingList == null) {
            return null;
        }
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        requestURI.replaceAll(requestURI, "").
                replaceAll("\\s", "").replaceAll("/+", "/");
        for (XQHandlerMapping xqHandlerMapping : handlerMappingList) {
            Matcher matcher = xqHandlerMapping.getPattern().matcher(requestURI);
            if (!matcher.matches()) {
                continue;
            }
            return xqHandlerMapping;
        }
        return null;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
