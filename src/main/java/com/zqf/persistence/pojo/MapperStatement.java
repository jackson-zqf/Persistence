package com.zqf.persistence.pojo;

import lombok.Data;

@Data
public class MapperStatement {

    //id
    private String id;
    //输入参数类型   这里String类型可以 优化为 Class 类型
    private String  paramterType;
    //输出参数类型   这里String类型可以 优化为 Class 类型
    private String resultType;
    //sql语句
    private String sql;

    private String sqlType;

}
