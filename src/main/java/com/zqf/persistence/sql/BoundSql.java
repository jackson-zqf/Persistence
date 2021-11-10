package com.zqf.persistence.sql;

import com.zqf.persistence.utils.ParameterMapping;
import lombok.Data;

import java.util.List;

@Data
public class BoundSql {


    public BoundSql(String sqlText, List<ParameterMapping> parameterMappings) {
        this.sqlText = sqlText;
        this.parameterMappings = parameterMappings;
    }

    private  String  sqlText;

    private List<ParameterMapping> parameterMappings;

}
