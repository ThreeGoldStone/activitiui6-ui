package com.yw56.javaservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yw56.javaservice.bean.JsonNodeType;
import com.yw56.javaservice.bean.JsonTreeBean;
import com.yw56.javaservice.bean.JsonTreeBean;
import com.yw56.javaservice.bean.ValueConfig;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yw56.javaservice.PropertiesUtils.extProperties;

public class ExtJSService implements JavaDelegate {
    public static final String EXTJS_VARIABLE_PREFIX = "extjspppp";
    public static final String KEY_FORM_VARIABLES = "key_form_variables";
    private FixedValue extjsserviceid;
    private FixedValue extjsservicerequestparam;
    private FixedValue extjsserviceresultset;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExtJSService.class);
    @Inject
    protected ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) {
        String tenantId = execution.getTenantId();
        String url = extProperties.getProperty("interfacesforward.url") + "?tenantId=" +
                tenantId +
                "&serviceId=" +
                getServiceId();

        LOGGER.info(tenantId);
        LOGGER.info(extjsserviceid.getExpressionText());
        LOGGER.info(extjsservicerequestparam.getExpressionText());
        LOGGER.info(extjsserviceresultset.getExpressionText());

        String requestBody = formRequestBody(execution);
//        ResponseEntity response = request(execution, url, requestBody);
        LOGGER.info("请求参数为 >>>>> " + requestBody);
        String data = request(execution, url, requestBody);
//        String data = response.getBody().toString();
        setResponseToEvn(data, execution);
        Map<String, Object> variables = execution.getVariables();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(variables);
        LOGGER.info("全局json参数为 >>>>> " + jsonObject.toJSONString());
//       throw new RuntimeException();
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(1 / 0);
    }

    private String request(DelegateExecution execution, String url, String requestBody) {
//        RestTemplate restTemplate = new RestTemplate(PropertiesUtils.requestFactory);
//        HttpMethod method = HttpMethod.POST;
//        execution.setVariable("times", execution.getVariable("times") + "+1");
//        Class responseType = String.class;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity requestEntity = new HttpEntity<String>(requestBody, headers);
//        ResponseEntity responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
//        String data = responseEntity.getBody().toString();
//        return data;
        String serviceId = getServiceId();
//        switch (serviceId) {
//            case "5":
//
//                break;
//            case "6":
//                break;
//            case "7":
//                break;
//        }
        String fileNameRes = serviceId + "/response.json";
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileNameRes);
            byte[] b = new byte[resourceAsStream.available()];
            resourceAsStream.read(b);
            return new String(b, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getServiceId() {
        // TODO 生成serviceid
        String expressionText = extjsserviceid.getExpressionText();
        JSONObject parse = JSON.parseObject(expressionText);
        String serviceid = parse.getString("serviceid");
        return serviceid;
    }

    /**
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
     *
     * @param data
     * @param execution
     */
    private void setResponseToEvn(String data, DelegateExecution execution) {
        // 配置的结果处理列表
        String resultSetString = extjsserviceresultset.getExpressionText();
        if (StringUtils.isBlank(resultSetString) || (!resultSetString.startsWith("["))) return;
        JSONArray resultSet = JSON.parseArray(resultSetString);
        String currentActivityId = execution.getCurrentActivityId();
        // 写入全局的最终结果
        JSONObject resultsToGlobal = new JSONObject();
        // 接口返回的json数据

        LOGGER.info(data);
        JSONObject result = JSON.parseObject(data);

        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject paramSet = resultSet.getJSONObject(i);
            String name = paramSet.getString("name");
            String type = paramSet.getString("type");
            String valuePath = paramSet.getString("valuePath");
            Object templateValue = paramSet.get("templateValue");
            Object value = getValueFromJsonByPath(result, valuePath);
            JsonNodeType jsonNodeType = JsonTreeBean.parseJsonNodeType(value);
            if (value == null || !type.equals(jsonNodeType.name())) {
                // TODO 结果设置时数据类型不匹配时的处理
            }
            resultsToGlobal.put(name, value);
        }
        execution.setVariable(EXTJS_VARIABLE_PREFIX + currentActivityId, resultsToGlobal);
    }

    private Object getValueFromJsonByPath(Object result, String valuePath) {
        String[] valuePathNodes = valuePath.split("\\.");
        Object node = result;
        for (int i = 0; i < valuePathNodes.length; i++) {
            String valuePathNode = valuePathNodes[i];
            if (StringUtils.isBlank(valuePathNode)) continue;
            if (node instanceof Long || node instanceof Integer || node instanceof BigDecimal) {
                return node;
            } else if (node instanceof String) {
                return node;
            } else if (node instanceof Boolean) {
                return node;
            } else if (node instanceof JSONArray) {
                JSONArray arrayNode = (JSONArray) node;
                if (arrayNode == null) return null;
                // 数组的下级路径：*代表遍历下级生成数据数据，数字代表数组的index，取单只返回
                if ("*".equals(valuePathNodes)) {
                    JSONArray arrayResult = new JSONArray();
//                    for (int j = 0; j < arrayNode.size(); j++) {
//                        Object innerNode = arrayNode.get(j);
//
//                    }
                    // TODO 根据路径取值，array路径后带*时的数据取值
                    return arrayNode;
                } else {
                    // 默认单值从0处取
                    int index = 0;
                    try {
                        index = Integer.parseInt(valuePathNode);
                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
                    }
                    node = arrayNode.get(index);
                }
            } else if (node instanceof JSONObject) {
                JSONObject node1 = (JSONObject) node;
                if (node1 != null) {
                    node = node1.get(valuePathNode);
                } else {
                    return null;
                }
            }
        }
        return node;
    }

    private String formRequestBody(DelegateExecution execution) {
        JSONObject variableRoot = getVariableRootFromExecution(execution);
        // 配置的结果处理列表
        String requestParamString = extjsservicerequestparam.getExpressionText();
        List<JsonTreeBean> requestParams = JSON.parseArray(requestParamString, JsonTreeBean.class);
        // 请求的json
        JSONObject requestBody = new JSONObject();
        diGuiFormRequestParam(requestParams, requestBody, variableRoot);
        return requestBody.toJSONString();
    }

    private void diGuiFormRequestParam(List<JsonTreeBean> requestParams, JSONObject requestBody, JSONObject variableRoot) {
        for (JsonTreeBean requestParam : requestParams) {
            ArrayList<JsonTreeBean> children = requestParam.getChildren();
            ValueConfig valueConfig = requestParam.getValueConfig();
            String name = requestParam.getName();
            if (valueConfig != null && !StringUtils.isBlank(valueConfig.getPath())) {
                String stencilid = valueConfig.getStencilid();
                String resourceId = valueConfig.getResourceId();
                String valuePath = valueConfig.getPath();

                Object subRoot = null;
                switch (stencilid) {
                    // extjs服务的参数
                    case "EXTJSServiceTask":
                        subRoot = variableRoot.get(EXTJS_VARIABLE_PREFIX + resourceId);
                        break;
                    // 默认其他的直取参数
                    default:
                        subRoot = variableRoot.get(KEY_FORM_VARIABLES);
                }
                Object valueFromJsonByPath = getValueFromJsonByPath(subRoot, valuePath);

                requestBody.put(name, valueFromJsonByPath);
            } else if (children != null && children.size() > 0) {
                // 递归子列表
                JsonNodeType type = requestParam.getType();
                switch (type) {
                    case object:
                        JSONObject jsonObject = new JSONObject();
                        requestBody.put(name, jsonObject);
                        diGuiFormRequestParam(children, jsonObject, variableRoot);
                        break;
                    case array:
                        JSONArray array = new JSONArray();
                        requestBody.put(name, array);

                        // TODO JSONArray 的暂不处理
                        break;
                }
            }

        }
    }

    /**
     * 获得修正路径的全局参数集合
     *
     * @param execution
     * @return
     */
    private JSONObject getVariableRootFromExecution(DelegateExecution execution) {
        // 所有参数根节点
        JSONObject variableRoot = new JSONObject();
        // 所有的form参数根节点
        JSONObject variablesForm = new JSONObject();

        Map<String, Object> variables = execution.getVariables();
        for (Map.Entry<String, Object> variablePair : variables.entrySet()) {
            String key = variablePair.getKey();
            Object value = variablePair.getValue();
            if (key.startsWith(EXTJS_VARIABLE_PREFIX)) {
                // extjs服务存的变量
                variableRoot.put(key, value);
            } else {
                // 如果是string类型的，就需要判断是否为json，如果是json就设置json对象或数组
                if (value instanceof String) {
                    String fs = (String) value;
                    try {
                        value = JSONObject.parse(fs);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
                variablesForm.put(key, value);
                // 其他form存的变量
            }

        }
        variableRoot.put(KEY_FORM_VARIABLES, variablesForm);
        return variableRoot;
    }

}
