package com.foryuum.backend.main.service;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service("mainService")
public class MainService {

    @Resource
    private SqlSessionTemplate sqlSession;

    public Map<String, Object> getUserInfo(String id) {
        return sqlSession.selectOne("main.GET_USER_INFO", id);
    }
}
