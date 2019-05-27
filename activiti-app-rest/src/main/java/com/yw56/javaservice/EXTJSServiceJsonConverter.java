package com.yw56.javaservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.language.json.converter.ServiceTaskJsonConverter;

import java.util.Map;

public class EXTJSServiceJsonConverter extends ServiceTaskJsonConverter {
    public static String FIELD_NAME_SERVICE_ID = "extjsserviceid";
    public static String FIELD_NAME_REQUEST_PARAMS = "extjsservicerequestparam";
    public static String FIELD_NAME_RESPONSE_HANDLERS = "extjsserviceresultset";

    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        // TODO
        FlowElement flowElement = super.convertJsonToElement(elementNode, modelNode, shapeMap);
        return flowElement;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        // TODO
        super.convertElementToJson(propertiesNode, baseElement);
    }
}
