package org.activiti.app.rest.editor;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Classname MyStaticBean
 * @Description TODO
 * @Date 2019/7/1 9:30
 * @Created by djl20
 */
@Component
public class MyStaticBean {
    @Autowired
    public RuntimeService runtimeService;
    private static MyStaticBean instance;

    public MyStaticBean() {
        System.out.println("MyStaticBean create");
        instance = this;
    }

    public static MyStaticBean getInstance() {
        return instance;
    }
}
