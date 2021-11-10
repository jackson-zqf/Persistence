package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.pojo.MapperStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration,Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object... params) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        List<Object> query = executor.query(configuration, mapperStatement, params);
        return (List<T>) query;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects != null && objects.size() == 1) {
            return (T) objects.get(0);
        }
        if(objects.size() == 0){
            return null;
        }
        throw new RuntimeException("结果集返回异常");
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        return  update(statementId,params);
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        return executor.update(configuration,mapperStatement,params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        return update(statementId,params);
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        return configuration.<T>getMapper(mapperClass,this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
