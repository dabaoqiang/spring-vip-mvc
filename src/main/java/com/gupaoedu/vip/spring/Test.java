package com.gupaoedu.vip.spring;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * @author xiaoqiang
 * @Title: Test
 * @ProjectName spring-vip-mvc
 * @Description: TODO
 * @date 2019-01-17 23:17
 */
public class Test {

    public void testa(@TestAnnocation(pat = "5") String s,
                      @TestAnnocation2 @TestAnnocation(pat = "6") String s1,
                      String s3,
                      @TestAnnocation(pat = "9") String s4) {
        System.out.println("------------" + s);
    }


    public static void main(String[] args) {
        Class<Test> testClass = Test.class;

        Method[] methods = testClass.getMethods();

        for (Method method : methods) {
            if ("testa".equals(method.getName())) {

                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                    for (Annotation an2 : parameterAnnotation) {
                        System.out.println(an2.annotationType().getSimpleName());
                        if (an2 instanceof TestAnnocation) {
                            TestAnnocation an21 = (TestAnnocation) an2;
                            System.out.println(an21.pat());
                        }
                    }

                }

            }

        }
    }

}
