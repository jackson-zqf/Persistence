package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{


    private Configuration configuration;
    private Executor executor = new SimpleExecutor();
    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }


    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration,executor);
    }
}
