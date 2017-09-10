package com.example.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StatAnnotation {

    int interval() default 60000;
    String name();
    String className() default "";
    String beanName();
    Class statProperty();

}
