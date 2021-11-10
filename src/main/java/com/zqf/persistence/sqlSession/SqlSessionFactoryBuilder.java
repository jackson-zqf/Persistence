package com.zqf.persistence.sqlSession;

import com.zqf.persistence.config.XmlConfigBuilder;
import com.zqf.persistence.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {


    /**
     * 返回 SqlSessionFactory 对象
     * @param inputStream
     * @return
     */
    public SqlSessionFactory  build(InputStream inputStream) throws Exception {

        //1.通过dom4j 解析配置信息的字节流数据，封装成Configuration对象.
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);
        //2.返回一个 SqlSessionFactory 对象
        return build(configuration);
    }


    public SqlSessionFactory build(Configuration configuration){
        return  new DefaultSqlSessionFactory(configuration);
    }
}
