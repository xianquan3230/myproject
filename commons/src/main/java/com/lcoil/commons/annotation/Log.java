package com.lcoil.commons.annotation;

import java.lang.annotation.*;

/**
 * @author lcoil
 * @create 2020-05-24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**模块*/
    String module() default "";

    /**描述*/
    String description() default "";
}