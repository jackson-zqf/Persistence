package com.zqf.persistence.pojo;

import com.zqf.persistence.proxy.MapperRegistry;
import com.zqf.persistence.sqlSession.SqlSession;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Data
public class Configuration {

    //数据库配置信息对象，将配置信息封装成对象。
    private DataSource dataSource;

    //Mapper注册表
    private MapperRegistry mapperRegistry = new MapperRegistry();

    /**
     * 将MapperStatement对象定义到Configuration中，需要向下传递参数时，只需要Configuration对象即可。
     *
     *  key: namespace.id  sql的唯一标识
     */
    Map<String,MapperStatement> mapperStatementMap = new HashMap<>();



    public <T> void  addMappers(Class<T> clz){

        mapperRegistry.addMappers(clz);
    }

    public <T> T getMapper(Class<?> clz, SqlSession sqlSession){
       return mapperRegistry.getMapper(clz,sqlSession);
    }

}
