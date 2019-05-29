package com.yw56.javaservice;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Map;

public class ExtJSService implements JavaDelegate {
    private FixedValue extjsserviceid;
    private FixedValue extjsservicerequestparam;
    private FixedValue extjsserviceresultset;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExtJSService.class);

    @Override
    public void execute(DelegateExecution execution) {
        String tenantId = execution.getTenantId();
        LOGGER.info(tenantId);
        LOGGER.info(extjsserviceid.getExpressionText());
        LOGGER.info(extjsservicerequestparam.getExpressionText());
        LOGGER.info(extjsserviceresultset.getExpressionText());
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(10 * 1000);
//        requestFactory.setReadTimeout(10 * 1000);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        String url = "";
//        String requestBody = "";
//        HttpMethod method = HttpMethod.POST;
//
//        Class responseType = String.class;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity requestEntity = new HttpEntity<String>(requestBody, headers);
//        ResponseEntity response = restTemplate.exchange(url, method, requestEntity, responseType);
    }

}
