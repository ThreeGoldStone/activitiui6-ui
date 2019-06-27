package com.yw56.javaservice;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

/**
 * @Classname LogListener
 * @Description TODO
 * @Date 2019/6/24 16:57
 * @Created by djl20
 */
public class LogListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println(Thread.currentThread().getId()+"  "+execution.getCurrentActivityId() + ">>> listener >>> " + execution.getEventName());
    }
//    @Override
//    public void notify(DelegateTask delegateTask) {
//        String eventName = delegateTask.getEventName();
//        System.out.println(delegateTask.getTaskDefinitionKey() + ">>> listener >>> " + eventName);
//    }
}
