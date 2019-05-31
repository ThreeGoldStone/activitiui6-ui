package com.yw56.javaservice;

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

        Class responseType = String.class;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity response = restTemplate.exchange(url, method, requestEntity, responseType);
        setResponseToEvn(response);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(1 / 0);
    }

    private String getServiceId() {
        // TODO 生成serviceid
        return null;
    }

    private void setResponseToEvn(ResponseEntity response) {
        LOGGER.info(response.getBody().toString());
        // TODO 把结果设置为环境变量
    }

    private String formRequestBody(DelegateExecution execution) {
        // TODO 根据设置的规则转化生成请求报文
        return null;
    }

}
