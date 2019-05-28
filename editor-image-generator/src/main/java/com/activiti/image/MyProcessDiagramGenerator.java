package com.activiti.image;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

/**
 * @Classname MyProcessDiagramGenerator
 * @Description 扩展默认的图生成器，增加自定义的控件
 * @Date 2019/5/28 15:58
 * @Created by djl20
 */
public class MyProcessDiagramGenerator extends DefaultProcessDiagramGenerator {
    public MyProcessDiagramGenerator() {
        this(1.0);
    }

    public MyProcessDiagramGenerator(final double scaleFactor) {
        super(scaleFactor);
        // EXTJS task
        activityDrawInstructions.put(ExtJSServiceTask.class, new ActivityDrawInstruction() {

            public void draw(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode) {
                GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
                ServiceTask serviceTask = (ServiceTask) flowNode;
                if ("camel".equalsIgnoreCase(serviceTask.getType())) {
                    processDiagramCanvas.drawCamelTask(serviceTask.getName(), graphicInfo, scaleFactor);
                } else if ("mule".equalsIgnoreCase(serviceTask.getType())) {
                    processDiagramCanvas.drawMuleTask(serviceTask.getName(), graphicInfo, scaleFactor);
                } else {
                    processDiagramCanvas.drawServiceTask(serviceTask.getName(), graphicInfo, scaleFactor);
                }
            }
        });
    }
}
