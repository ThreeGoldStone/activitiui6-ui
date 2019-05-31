package com.yw56.javaservice;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @Classname PropertiesUtils
 * @Description TODO
 * @Date 2019/5/29 17:21
 * @Created by djl20
 */
public class PropertiesUtils {
    public static Properties extProperties = new Properties();
    public static SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    static {
        try {
            extProperties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("extjs.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestFactory.setConnectTimeout(Integer.parseInt(extProperties.getProperty("interfacesforward.connectTimeout", "5000")));
        requestFactory.setReadTimeout(Integer.parseInt(extProperties.getProperty("interfacesforward.readTimeout", "5000")));
    }
}
