package com.foryuum.backend.taobao.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.foryuum.backend.taobao.service.TaobaoService;

import jakarta.annotation.Resource;

@RestController
public class TaobaoController {

    @Resource(name = "assetService")
    private TaobaoService assetService;

    @PostMapping("/serachTaobaoImage")
    public Map<String, Object> getMethodName(@RequestBody Map<String, Object> paramData) {
        System.out.println(paramData);

        try {
            MultipartFile file = (MultipartFile) paramData.get("uploadImg");
            Path filePath = Paths.get("C:\\Users\\Yoon\\Desktop\\uploadTes/" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        Map<String, Object> responseData = Map.of("message", "Data received successfully");
        return responseData;
    }

}
