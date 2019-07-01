package com.yw56.javaservice;

import org.activiti.app.rest.editor.MyStaticBean;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname SuspendService
 * @Description TODO
 * @Date 2019/6/30 15:52
 * @Created by djl20
 */
public class SuspendService implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        System.out.println("开始执行流程 SuspendService :" + currentActivityId);
        Object isSuspend = execution.getVariable("isSuspend");
        Object isException = execution.getVariable("isException");
        Integer sleepSecond = (Integer) execution.getVariable("sleepSecond");

        String processInstanceId = execution.getProcessInstanceId();
        if (isSuspend != null && isSuspend.toString().equalsIgnoreCase("true")) {
            System.out.println("开始暂停流程实例");
            MyStaticBean.getInstance().runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println();
        }

        if (sleepSecond != null) {
            System.out.println("sleep  >>> " + sleepSecond);
            try {
                Thread.sleep(sleepSecond * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isException != null && isException.toString().equalsIgnoreCase("true")) {
            throw new ActivitiException("简单的抛个错");
        }
        System.out.println("结束执行 SuspendService :" + currentActivityId);
    }
}
