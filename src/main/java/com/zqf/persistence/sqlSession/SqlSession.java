package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;

import java.util.List;

public interface SqlSession {

    /**
     * 查询所有
     *
     * @param statementId  sql的唯一标识，通过这个唯一标识可以获取到MapperStatement对象
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String statementId, Object... params) throws Exception;


    /**
     * 查询单条数据
     *
     * @param statementId
     * @param params
     * @param <T>
     * @return
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;


    /**
     * 插入单条数据
     * @param statementId
     * @param params
     * @return
     */
    int insert(String statementId , Object... params) throws Exception;

    /**
     * 更新单条数据
     * @param statementId
     * @param params
     * @return
     */
    int update(String statementId,Object... params) throws Exception;

    /**
     * 删除数据
     * @param statementId
     * @param params
     * @return
     */
    int delete(String statementId,Object... params) throws Exception;

    /**
     * 调用此方法返回一个 代理对象
     * @param mapperClass
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T>  mapperClass);

    /**
     * 返回Configuration 对象
     * @return
     */
    Configuration getConfiguration();
}
