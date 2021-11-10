package com.zqf.persistence.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zqf.persistence.pojo.Configuration;
import com.zqf.persistence.resources.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XmlConfigBuilder {

    private Configuration configuration;


    public XmlConfigBuilder(){
        this.configuration = new Configuration();
    }

    /**
     * 解析配置文件字节流 封装成Configuration 对象
     * @param inputStream
     * @return
     */
    public Configuration  parseConfig(InputStream inputStream) throws Exception {
        //使用dom4j 解析配置文件
        //解析 数据库配置文件信息 sqlMapConfig.xml
        Document document = new SAXReader().read(inputStream);

        //拿到配置文件 根标签  <configuration>
        Element rootElement = document.getRootElement();

        //拿到所有property的 标签行
        List<Element> list = rootElement.selectNodes("//property");

        Properties properties = new Properties();
        //遍历集合
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            //将所有 property 的属性值放到 properties对象中
            properties.setProperty(name,value);
        }

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);


        //解析mapper.xml:  拿到路径--解析成字节流--封装成MapperStatement对象
        List<Element> elements = rootElement.selectNodes("//mapper");
        for (Element element : elements) {
            mapperElement(element);
        }
        return configuration;
    }


    private void  mapperElement(Element element) throws Exception {
        String mapperPath = element.attributeValue("resource");
        InputStream resourcesAsStream = Resources.getResourcesAsStream(mapperPath);
        XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
        xmlMapperBuilder.parse(resourcesAsStream);
    }
}
