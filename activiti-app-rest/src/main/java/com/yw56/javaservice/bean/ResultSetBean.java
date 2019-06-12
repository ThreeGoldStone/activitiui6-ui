package com.yw56.javaservice.bean;

import java.io.Serializable;

/**
 * @Classname ResultSetBean
 * @Description 把extjsservice的返回值设置为全局参数的配置
 * [
 * {
 * "name": "fieldName1",            设置的全局参数的key
 * "implementation": "p133",        全局参数引用的参数名
 * "valuePath": "",                 引用参数的参数路径
 * "valueType": "",                 引用参数值的类型
 * "implementationid": 6,           引用参数的id
 * },
 * {
 * "name": "fieldName2",
 * "implementation": "p13",
 * "valuePath": "",
 * "valueType": "",
 * "implementationid": 3,
 * }
 * ]
 * @Date 2019/6/11 18:11
 * @Created by djl20
 */
public class ResultSetBean implements Serializable {
    private String name;
    private String implementation;
    private String valuePath;
    private String valueType;
    private String implementationid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getValuePath() {
        return valuePath;
    }

    public void setValuePath(String valuePath) {
        this.valuePath = valuePath;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getImplementationid() {
        return implementationid;
    }

    public void setImplementationid(String implementationid) {
        this.implementationid = implementationid;
    }
}
