package com.yw56.javaservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.ServiceTaskJsonConverter;

import java.util.Map;

public class EXTJSServiceJsonConverter extends ServiceTaskJsonConverter implements EXTJSConstants {

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap, Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }

    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_EXTJS_TASK_SERVICE, EXTJSServiceJsonConverter.class);
    }

    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(ServiceTask.class, EXTJSServiceJsonConverter.class);
    }

    @Override
    protected String getStencilId(BaseElement baseElement) {
        return STENCIL_EXTJS_TASK_SERVICE;
    }

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        // TODO
        ServiceTask extJSServiceTask = (ServiceTask) super.convertJsonToElement(elementNode, modelNode, shapeMap);
        FieldExtension fieldID = new FieldExtension();
        fieldID.setFieldName(FIELD_NAME_SERVICE_ID);
        String vss1 = _getPropertyValueAsString(FIELD_NAME_SERVICE_ID, elementNode);
        fieldID.setStringValue(vss1);
        FieldExtension fieldReq = new FieldExtension();
        fieldReq.setFieldName(FIELD_NAME_REQUEST_PARAMS);
        fieldReq.setStringValue(_getPropertyValueAsString(FIELD_NAME_REQUEST_PARAMS, elementNode));
        FieldExtension fieldRep = new FieldExtension();
        fieldRep.setFieldName(FIELD_NAME_RESPONSE_HANDLERS);
        fieldRep.setStringValue(_getPropertyValueAsString(FIELD_NAME_RESPONSE_HANDLERS, elementNode));
        extJSServiceTask.getFieldExtensions().add(fieldID);
        extJSServiceTask.getFieldExtensions().add(fieldReq);
        extJSServiceTask.getFieldExtensions().add(fieldRep);
        // 设置默认的处理类
        extJSServiceTask.setImplementation("com.yw56.javaservice.ExtJSService");
        extJSServiceTask.setImplementationType("class");
        return extJSServiceTask;
    }

    private String _getPropertyValueAsString(String fieldNameServiceId, JsonNode elementNode) {
        JsonNode properties = elementNode.get(EDITOR_SHAPE_PROPERTIES);
        JsonNode property = properties.get(fieldNameServiceId);
        String s = property.toString();
        return s;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        super.convertElementToJson(propertiesNode, baseElement);
        // 如果处理类为com.yw56.javaservice.ExtJSService，则解析为SERVICE_NAME_EXTJS_SERVICE_TASK
        ServiceTask serviceTask = (ServiceTask) baseElement;
    }
}
