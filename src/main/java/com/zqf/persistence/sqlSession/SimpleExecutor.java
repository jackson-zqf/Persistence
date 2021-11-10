package com.zqf.persistence.sqlSession;

import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.pojo.MapperStatement;
import com.zqf.persistence.sql.BoundSql;
import com.zqf.persistence.utils.GenericTokenParser;
import com.zqf.persistence.utils.ParameterMapping;
import com.zqf.persistence.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    @Override
    public <T> List<T> query(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception {

        BoundSql boundSql = getBoundSql(mapperStatement);
        PreparedStatement preparedStatement = getPreparedStatement(getConnection(configuration) ,boundSql);
        ResultSet resultSet = doQuery(mapperStatement, boundSql, preparedStatement, params);

        //6.封装结果集
        //找到对应返回结果集的 Class类型
        String resultType = mapperStatement.getResultType();
        Class<?> resultTypeClass = getParamterTypeClass(resultType);
        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            //结果集元数据
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //查询结果集中 返回的总列数
            for (int i = 1; i <=resultSetMetaData.getColumnCount(); i++) {
                //某一列的 字段名称 id
                String columnName = resultSetMetaData.getColumnName(i);
                //对应那一列的 字段值 1
                Object columnValue = resultSet.getObject(columnName);
                //使用反射或内省方式，将值映射到 返回结果对象上。 这里使用内省
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, columnValue);
            }
            objects.add(o);
        }

        return (List<T>) objects;
    }

    @Override
    public int insert(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception {
        return  update(configuration,mapperStatement,params);
    }

    @Override
    public int update(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception {

        BoundSql boundSql = getBoundSql(mapperStatement);
        PreparedStatement preparedStatement = getPreparedStatement(getConnection(configuration) ,boundSql);
        return doUpdate(mapperStatement,boundSql,preparedStatement,params);
    }

    @Override
    public int delete(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception {
        return update(configuration,mapperStatement,params);
    }

    private  int doUpdate(MapperStatement mapperStatement,BoundSql boundSql,PreparedStatement preparedStatement,Object... params) throws  Exception{
        String paramterType = mapperStatement.getParamterType();
        Class<?> paramterTypeClass = getParamterTypeClass(paramterType);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            //拿到#{} 中的属性值  id  username
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (paramterTypeClass == String.class) {
                preparedStatement.setString(i + 1, (String) params[i]);
            } else if (paramterTypeClass == Integer.class) {
                preparedStatement.setInt(i + 1, (Integer) params[i]);
            } else {
                //通过反射获取到 Field字段
                Field declaredField = paramterTypeClass.getDeclaredField(parameterMapping.getContent());
                declaredField.setAccessible(true);
                //获取到字段的值 1  张三
                Object o = declaredField.get(params[0]);
                //赋值到 preparestatement中
                preparedStatement.setObject(i + 1, o);
            }
        }
        return preparedStatement.executeUpdate();
    }

    private ResultSet  doQuery(MapperStatement mapperStatement,BoundSql boundSql,PreparedStatement preparedStatement,Object... params) throws Exception {
        String paramterType = mapperStatement.getParamterType();
        Class<?> paramterTypeClass = getParamterTypeClass(paramterType);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {

            //拿到#{} 中的属性值  id  username
            ParameterMapping parameterMapping = parameterMappings.get(i);

            //通过反射获取到 Field字段
            Field declaredField = paramterTypeClass.getDeclaredField(parameterMapping.getContent());
            declaredField.setAccessible(true);
            //获取到字段的值 1  张三
            Object o = declaredField.get(params[0]);
            //赋值到 preparestatement中
            preparedStatement.setObject(i+1, o);
        }

        //5. 执行sql 获取结果集
        return preparedStatement.executeQuery();
    }

    private BoundSql getBoundSql(MapperStatement mapperStatement) throws Exception {
        //2.获得sql 语句
        //现在sql存在mapperStatement中，且格式为 select * from user  where id = #{id} and  username = #{username};
        //需要转化格式 ，转换为   select * from user  where id = ? and  username = ?; 并且解析出#{id}参数名称
        String sql = mapperStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        return boundSql;
    }

    private Connection getConnection(Configuration configuration) throws Exception {
        //1.注册驱动，获取数据库连接
       return configuration.getDataSource().getConnection();
    }

    private PreparedStatement  getPreparedStatement(Connection connection,BoundSql boundSql) throws SQLException {
         return  connection.prepareStatement(boundSql.getSqlText());
    }



    private Class<?> getParamterTypeClass(String paramterType) throws ClassNotFoundException {
        if (paramterType != null) {
            return Class.forName(paramterType);
        }
        return null;
    }


    /**
     * 将#{} 标记进行解析 : 1.对#{}标记 替换为 ? 2. 将#{}中的 属性值存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {

        //参数处理器,将#{}中的参数值解析出来放到 ParameterMappingTokenHandler 中去
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //标记解析器，将#{} 替换为 ? 并且返回 替换完后的sql语句
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);

        BoundSql boundSql = new BoundSql(parseSql, parameterMappingTokenHandler.getParameterMappings());
        return boundSql;
    }
}
