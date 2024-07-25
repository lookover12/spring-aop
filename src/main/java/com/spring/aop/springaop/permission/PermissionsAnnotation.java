package com.spring.aop.springaop.permission;

import java.lang.annotation.*;

/**
 * 鉴权注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionsAnnotation {
}
