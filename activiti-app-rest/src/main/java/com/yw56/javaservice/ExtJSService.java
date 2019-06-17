package com.yw56.javaservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yw56.javaservice.bean.JsonNodeType;
import com.yw56.javaservice.bean.JsonTreeBean;
import com.yw56.javaservice.bean.RequestParamBean;
import com.yw56.javaservice.bean.ValueConfig;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static com.yw56.javaservice.PropertiesUtils.extProperties;

public class ExtJSService implements JavaDelegate {
    private FixedValue extjsserviceid;
    private FixedValue extjsservicerequestparam;
    private FixedValue extjsserviceresultset;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExtJSService.class);

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

        RestTemplate restTemplate = new RestTemplate(PropertiesUtils.requestFactory);
        String requestBody = formRequestBody(execution);
        HttpMethod method = HttpMethod.POST;
        execution.setVariable("times", execution.getVariable("times") + "+1");
        Class responseType = String.class;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity response = restTemplate.exchange(url, method, requestEntity, responseType);
        setResponseToEvn(response, execution);
//       throw new RuntimeException();
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(1 / 0);
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
     * @param response
     * @param execution
     */
    private void setResponseToEvn(ResponseEntity response, DelegateExecution execution) {
        // 配置的结果处理列表
        String resultSetString = extjsserviceresultset.getExpressionText();
        JSONArray resultSet = JSON.parseArray(resultSetString);
        String currentActivityId = execution.getCurrentActivityId();
        // 写入全局的最终结果
        JSONObject resultsToGlobal = new JSONObject();
        // 接口返回的json数据
        String data = response.getBody().toString();
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
        execution.setVariable(currentActivityId, resultsToGlobal);
    }

    private Object getValueFromJsonByPath(Object result, String valuePath) {
        String[] valuePathNodes = valuePath.split(".");
        Object node = result;
        for (int i = 0; i < valuePathNodes.length; i++) {
            String valuePathNode = valuePathNodes[i];
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
                    return arrayResult;
                } else {
                    // 默认单值从0处取
                    int index = 0;
                    try {
                        index = Integer.parseInt(valuePathNode);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
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
        // 配置的结果处理列表
        String requestParamString = extjsservicerequestparam.getExpressionText();
        List<RequestParamBean> requestParams = JSON.parseArray(requestParamString, RequestParamBean.class);
        // 请求的json
        JSONObject requestBody = new JSONObject();
        for (RequestParamBean requestParam : requestParams) {
            Object value = null;
            ValueConfig valueConfig = requestParam.getValueConfig();
            if (valueConfig != null && !StringUtils.isBlank(valueConfig.getPath())) {
                String stencilid = valueConfig.getStencilid();
                String resourceId = valueConfig.getResourceId();
                String valuePath = valueConfig.getPath();
                switch (stencilid) {
                    case "EXTJSServiceTask":
                        JSONObject variable = (JSONObject) execution.getVariable(resourceId);
                        value = getValueFromJsonByPath(variable, valuePath);
                        break;
                    default:
                        String[] paths = valuePath.split(".");
                        String formName = paths[0];
                        Object fv = execution.getVariable(formName);
                        Object fj = null;
                        if (fv instanceof String) {
                            String fs = (String) fv;
                            try {
                                fj = JSONObject.parse(fs);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (fj != null) {
                                if (paths.length > 1) {
                                    value = getValueFromJsonByPath(fj, valuePath.replace("." + formName, ""));
                                } else {
                                    value = fj;
                                }
                            } else {
                                value = fs;
                            }
                        } else {
                            value = fv;
                        }

                        break;
                }
                // 递归子列表
                requestBody.put(requestParam.getName(), value);
            }

        }
        // TODO 根据设置的规则转化生成请求报文
        return null;
    }

}
