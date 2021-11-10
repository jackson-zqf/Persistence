package com.zqf.persistence.config;

import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlMapperBuilder {

    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析 mapper.xml 配置文件
     *
     * @param inputStream
     * @return
     */
    public Configuration parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");
        addMapper(namespace);

        List<Element> selectList = rootElement.selectNodes("//select");
        for (Element element : selectList) {
            selectElement(element,namespace);
        }

        List<Element> updateList = rootElement.selectNodes("//update");
        for (Element element : updateList) {
            ortherElement(element,namespace,"update");
        }

        List<Element> insertList = rootElement.selectNodes("//insert");
        for (Element element : insertList) {
            ortherElement(element,namespace,"insert");
        }

        List<Element> deleteList = rootElement.selectNodes("//delete");
        for (Element element : deleteList) {
            ortherElement(element,namespace,"delete");
        }
        return configuration;
    }





    public void  ortherElement(Element element,String namespace,String sqlType){
        MapperStatement mapperStatement = new MapperStatement();

        String  id = element.attributeValue("id");
        String paramterType = element.attributeValue("parameterType");
        String sql = element.getTextTrim();

        mapperStatement.setId(id);
        mapperStatement.setParamterType(paramterType);
        mapperStatement.setSql(sql);
        mapperStatement.setSqlType(sqlType);
        String key = namespace + "." + id;
        configuration.getMapperStatementMap().put(key,mapperStatement);
    }

    private void selectElement(Element element,String namespace) {
        MapperStatement mapperStatement = new MapperStatement();

        String id = element.attributeValue("id");
        String resultType = element.attributeValue("resultType");
        String paramterType = element.attributeValue("parameterType");
        String sql = element.getTextTrim();

        mapperStatement.setId(id);
        mapperStatement.setParamterType(paramterType);
        mapperStatement.setResultType(resultType);
        mapperStatement.setSql(sql);
        mapperStatement.setSqlType("select");
        String key = namespace + "." + id;
        configuration.getMapperStatementMap().put(key, mapperStatement);
    }

    private void addMapper(String nameSpace) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(nameSpace);
        configuration.addMappers(aClass);
    }
}
