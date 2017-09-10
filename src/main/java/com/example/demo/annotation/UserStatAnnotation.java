package com.example.demo.annotation;

import com.example.demo.log.property.BaseStatProperty;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserStatAnnotation {

    int interval() default 60000;
    String name() default "";
    String className() default "";
    Class statProperty();

}
