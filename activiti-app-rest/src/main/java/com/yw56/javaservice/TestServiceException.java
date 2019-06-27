package com.yw56.javaservice;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestServiceException implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(TestServiceException.class);
    int i = 0;

    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> variablesLocal = execution.getVariablesLocal();
        System.out.println("local variables  =  " + variablesLocal);
        Map<String, Object> variables = execution.getVariables();
        System.out.println("variables  =  " + variables);
        Map<String, VariableInstance> variableInstances = execution.getVariableInstances();
        System.out.println("test service 被执行 " + (i++));
        Object exc = execution.getVariable("exc");
        System.out.println("任务执行的线程id 》 " + Thread.currentThread().getId());
        if (exc != null) {
            throw new ActivitiException("我想抛个错");
        }
    }

}
