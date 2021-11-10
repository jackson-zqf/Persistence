package com.zqf.persistence.proxy;

import com.zqf.persistence.sqlSession.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private Map<Class<?>, MapperProxyFactory<?>> mappers = new HashMap<>();


    public <T> void addMappers(Class<T> clz) {
        mappers.put(clz, new MapperProxyFactory<T>(clz));
    }


    public <T> T getMapper(Class<?> clz, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) mappers.get(clz);
        return mapperProxyFactory.newInstance(sqlSession);
    }


}
