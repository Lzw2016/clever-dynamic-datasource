package org.clever.dynamic.datasource.annotation;


import java.lang.annotation.*;

/**
 * 注解在类上或方法上来切换数据源
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    /**
     * 数据源名称
     */
    String value() default "master";
}