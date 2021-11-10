package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.pojo.MapperStatement;

import java.util.List;

/**
 * 对jdbc 操作数据库的封装
 */
public interface Executor {

    <T> List<T> query(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception;

    int insert(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception;

    int update(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception;

    int delete(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception;
}
