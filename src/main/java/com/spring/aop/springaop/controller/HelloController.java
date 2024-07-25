package com.spring.aop.springaop.controller;

import com.spring.aop.springaop.permission.PermissionsAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @PermissionsAnnotation
    public String hello(@RequestParam String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello ").append(name).append("!");
        return stringBuilder.toString();
    }
}
