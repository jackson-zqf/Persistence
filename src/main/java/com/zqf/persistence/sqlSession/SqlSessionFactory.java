package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;

public interface SqlSessionFactory {

    SqlSession  openSession();
}
