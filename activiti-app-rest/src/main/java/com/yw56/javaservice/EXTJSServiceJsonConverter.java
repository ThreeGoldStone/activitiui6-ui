package com.yw56.javaservice;

import com.activiti.image.ExtJSServiceTask;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FieldExtension;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.ServiceTaskJsonConverter;

import java.util.Map;

public class EXTJSServiceJsonConverter extends ServiceTaskJsonConverter {
    public static String SERVICE_NAME_EXTJS_SERVICE_TASK = "EXTJSServiceTask";
    public static String FIELD_NAME_SERVICE_ID = "extjsserviceid";
    public static String FIELD_NAME_REQUEST_PARAMS = "extjsservicerequestparam";
    public static String FIELD_NAME_RESPONSE_HANDLERS = "extjsserviceresultset";

    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap, Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {

        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }

    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(SERVICE_NAME_EXTJS_SERVICE_TASK, EXTJSServiceJsonConverter.class);
    }

    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(ExtJSServiceTask.class, EXTJSServiceJsonConverter.class);
    }

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        // TODO
        ServiceTask flowElement = (ServiceTask) super.convertJsonToElement(elementNode, modelNode, shapeMap);
        ExtJSServiceTask extJSServiceTask = new ExtJSServiceTask();
        extJSServiceTask.setValues(flowElement);
        FieldExtension fieldID = new FieldExtension();
        fieldID.setFieldName(FIELD_NAME_SERVICE_ID);
        fieldID.setStringValue(getValueAsString(FIELD_NAME_SERVICE_ID, elementNode));
        FieldExtension fieldReq = new FieldExtension();
        fieldReq.setFieldName(FIELD_NAME_REQUEST_PARAMS);
        fieldReq.setStringValue(getValueAsString(FIELD_NAME_REQUEST_PARAMS, elementNode));
        FieldExtension fieldRep = new FieldExtension();
        fieldRep.setFieldName(FIELD_NAME_RESPONSE_HANDLERS);
        fieldRep.setStringValue(getValueAsString(FIELD_NAME_RESPONSE_HANDLERS, elementNode));
        extJSServiceTask.getFieldExtensions().add(fieldID);
        extJSServiceTask.getFieldExtensions().add(fieldReq);
        extJSServiceTask.getFieldExtensions().add(fieldRep);
        return extJSServiceTask;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        // TODO
        super.convertElementToJson(propertiesNode, baseElement);
    }
}
