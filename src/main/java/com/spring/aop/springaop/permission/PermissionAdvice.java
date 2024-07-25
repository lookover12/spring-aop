package com.spring.aop.springaop.permission;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * aop切面鉴权类
 */

@Aspect
@Component
@Slf4j
@Order(1)
public class PermissionAdvice {

    //不需要登录的地址
    @Value("${not.login.uri:hello}")
    public String notLoginUri;

    //有权限的用户
    @Value("${permission.user:zhangsan}")
    public String permissionUser;

    //切点定义
    @Pointcut("@annotation(com.spring.aop.springaop.permission.PermissionsAnnotation)")
    public void permissionPointcutCheck() {}

    @Before("permissionPointcutCheck()")
    public void beforeAdvice(JoinPoint joinPoint) {
        //1.先获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //2.获取请求中的接口(并校验是否需要校验登录信息)
        String uri = request.getRequestURI();
        if(checkUri(uri)) {
            return;
        }

        //3.开始校验用户是否有权限
        Cookie[] cookies = request.getCookies();
        String userName = getUserInfo(cookies);
        if(checkUserPermission(userName)) {
            return;
        }
        log.info("用户: {}没有访问地址{}的权限", userName, uri);
        //再抛出异常
    }

    //校验是否需要登录
    private boolean checkUri(String uri) {
        String[] uris = notLoginUri.split(",");
        for (String uri1 : uris) {
            if (uri.equals(uri1)) {
                return true;
            }
        }
        return false;
    }

    //校验当前用户是否有权限访问
    private boolean checkUserPermission(String user) {
        if(null == user) {
            return false;
        }

        String[] users = permissionUser.split(",");
        for (String user1 : users) {
            if(user.equals(user1)) {
                return true;
            }
        }
        return false;
    }

    //从cookie中获取用户信息
    private String getUserInfo(Cookie[] cookies) {
        if(cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
