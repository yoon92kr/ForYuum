package com.foryuum.backend.taobao.service;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service("assetService")
public class TaobaoService {

    @Resource
    private SqlSessionTemplate sqlSession;

    public Map<String, Object> getUserInfo(String id) {
        return sqlSession.selectOne("main.GET_USER_INFO", id);
    }
}
