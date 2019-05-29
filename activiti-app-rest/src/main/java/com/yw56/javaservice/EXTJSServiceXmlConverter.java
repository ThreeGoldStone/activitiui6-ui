//package com.yw56.javaservice;
//
//import com.activiti.image.ExtJSServiceTask;
//import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
//import org.activiti.bpmn.converter.ServiceTaskXMLConverter;
//
//import java.util.Map;
//
//public class EXTJSServiceXmlConverter extends ServiceTaskXMLConverter implements EXTJSConstants{
//
//
//    public static void fillTypes(Map<String, Class<? extends BaseBpmnXMLConverter>> convertersToBpmnMap, Map<Class<ExtJSServiceTask>, Class<? extends BaseBpmnXMLConverter>> convertersToJsonMap) {
//
//        fillJsonTypes(convertersToBpmnMap);
//        fillBpmnTypes(convertersToJsonMap);
//    }
//
//    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnXMLConverter>> convertersToBpmnMap) {
//        convertersToBpmnMap.put(STENCIL_EXTJS_TASK_SERVICE, EXTJSServiceXmlConverter.class);
//    }
//
//    public static void fillBpmnTypes(Map<Class<ExtJSServiceTask>, Class<? extends BaseBpmnXMLConverter>> convertersToJsonMap) {
//        convertersToJsonMap.put(ExtJSServiceTask.class, EXTJSServiceXmlConverter.class);
//    }
//
////    @Override
////    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
////        // TODO
////        ServiceTask flowElement = (ServiceTask) super.convertJsonToElement(elementNode, modelNode, shapeMap);
////        ExtJSServiceTask extJSServiceTask = new ExtJSServiceTask();
////        extJSServiceTask.setValues(flowElement);
////        FieldExtension fieldID = new FieldExtension();
////        fieldID.setFieldName(FIELD_NAME_SERVICE_ID);
////        String vss1 = _getPropertyValueAsString(FIELD_NAME_SERVICE_ID, elementNode);
////        fieldID.setStringValue(vss1);
////        FieldExtension fieldReq = new FieldExtension();
////        fieldReq.setFieldName(FIELD_NAME_REQUEST_PARAMS);
////        fieldReq.setStringValue(_getPropertyValueAsString(FIELD_NAME_REQUEST_PARAMS, elementNode));
////        FieldExtension fieldRep = new FieldExtension();
////        fieldRep.setFieldName(FIELD_NAME_RESPONSE_HANDLERS);
////        fieldRep.setStringValue(_getPropertyValueAsString(FIELD_NAME_RESPONSE_HANDLERS, elementNode));
////        extJSServiceTask.getFieldExtensions().add(fieldID);
////        extJSServiceTask.getFieldExtensions().add(fieldReq);
////        extJSServiceTask.getFieldExtensions().add(fieldRep);
////        return extJSServiceTask;
////    }
////
////    private String _getPropertyValueAsString(String fieldNameServiceId, JsonNode elementNode) {
////        JsonNode properties = elementNode.get(EDITOR_SHAPE_PROPERTIES);
////        JsonNode property = properties.get(fieldNameServiceId);
////        String s = property.toString();
////        return s;
////    }
////
////    @Override
////    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
////        // TODO
////        super.convertElementToJson(propertiesNode, baseElement);
////    }
//}
