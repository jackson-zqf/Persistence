package com.zqf.persistence.resources;

import java.io.InputStream;

public class Resources {

    /**
     * 将指定路径的资源解析为字节流
     * @param path
     * @return
     */
    public static InputStream getResourcesAsStream(String path){

        return  Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
