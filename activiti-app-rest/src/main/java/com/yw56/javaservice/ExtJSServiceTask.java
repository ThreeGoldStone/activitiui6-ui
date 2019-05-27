package com.yw56.javaservice;

import org.activiti.bpmn.model.ServiceTask;

public class ExtJSServiceTask extends ServiceTask {
    @Override
    public String getImplementation() {
        return ExtJSService.class.getName();
    }

    @Override
    public String getImplementationType() {
        return "class";
    }
}
