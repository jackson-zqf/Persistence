package com.zqf.persistence.proxy;

import com.zqf.persistence.sqlSession.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {

    private Class<T>  interfaceClass;


    public MapperProxyFactory(Class<T>  interfaceClass){
        this.interfaceClass = interfaceClass;
    }


    public T newInstance(MapperProxy<T> mapperProxy){
        T t = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[] { interfaceClass },mapperProxy);
       return t;
    }

    public T newInstance(SqlSession sqlSession){
        MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession,interfaceClass);
        return newInstance(mapperProxy);
    }
}
