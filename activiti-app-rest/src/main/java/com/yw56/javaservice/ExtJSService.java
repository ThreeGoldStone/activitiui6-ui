package com.yw56.javaservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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
        setResponseToEvn(response,execution);
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
     *   {
     *     "name": "fieldName1",            设置的全局参数的key
     *     "implementation": "p133",        全局参数引用的参数名
     *     "valuePath": "",                 引用参数的参数路径
     *     "valueType": "",                 引用参数值的类型
     *     "implementationid": 6,           引用参数的id
     *   },
     *   {
     *     "name": "fieldName2",
     *     "implementation": "p13",
     *     "valuePath": "",
     *     "valueType": "",
     *     "implementationid": 3,
     *   }
     * ]
     * @param response
     * @param execution
     */
    private void setResponseToEvn(ResponseEntity response, DelegateExecution execution) {
        String resultSetString = extjsserviceresultset.getExpressionText();
        String data = response.getBody().toString();
        LOGGER.info(data);
        JSONArray resultSet = JSON.parseArray(resultSetString);
        JSONObject result = JSON.parseObject(data);
        for (int i = 0; i < resultSet.size(); i++) {
            JSONObject paramSet = resultSet.getJSONObject(i);



        }
        // TODO 把结果设置为环境变量
    }

    private String formRequestBody(DelegateExecution execution) {
        // TODO 根据设置的规则转化生成请求报文
        return null;
    }

}
