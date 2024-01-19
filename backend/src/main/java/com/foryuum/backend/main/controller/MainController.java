package com.foryuum.backend.main.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foryuum.backend.main.service.MainService;

import jakarta.annotation.Resource;

@RestController
public class MainController {

    @Resource(name = "mainService")
    private MainService mainService;

    @GetMapping("/loginCheck")
    public String getMethodName(@RequestParam Map<String, Object> paramData) {
        String userId = "yuum";
        Map<String, Object> test = mainService.getUserInfo(userId);
        System.out.println(test);
        return null;
    }

}
