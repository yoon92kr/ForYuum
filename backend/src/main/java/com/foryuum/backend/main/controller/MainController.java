package com.foryuum.backend.main.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foryuum.backend.main.service.MainService;

import jakarta.annotation.Resource;

@RestController
public class MainController {

    @Resource(name = "mainService")
    private MainService mainService;

    @GetMapping("/")
    public String getMethodName() {
        String userId = "yuum";
        Map<String, Object> test = mainService.getUserInfo(userId);
        System.out.println(test);
        return "by";
    }

}
