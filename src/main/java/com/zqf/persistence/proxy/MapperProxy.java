package com.zqf.persistence.proxy;

import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.pojo.MapperStatement;
import com.zqf.persistence.sqlSession.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class MapperProxy<T> implements InvocationHandler, Serializable {

    private SqlSession sqlSession;
    private Class<T> interfaceClass;

    public MapperProxy(SqlSession sqlSession, Class<T> interfaceClass) {
        this.sqlSession = sqlSession;
        this.interfaceClass = interfaceClass;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Configuration configuration = sqlSession.getConfiguration();
        //方法名
        String name = method.getName();
        //类的全限定名
        String className = method.getDeclaringClass().getName();
        String statementId = className + "." + name;

        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        switch (mapperStatement.getSqlType()){
            case "select":
                Type genericParameterTypes = method.getGenericReturnType();
                //判断返回值是否 进行了  泛型类型参数化  就是判断是否存在泛型
                if (genericParameterTypes instanceof ParameterizedType) {
                    List<Object> objects = sqlSession.selectList(statementId, args);
                    return objects;
                }
                return sqlSession.selectOne(statementId, args);
            case "update":
                return  sqlSession.update(statementId,args);
            case "insert":
                return  sqlSession.insert(statementId,args);
            case "delete":
                return sqlSession.delete(statementId,args);
            default:
                break;
        }
        return  null;
    }


}
