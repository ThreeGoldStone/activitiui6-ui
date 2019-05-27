package com.yw56.javaservice;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

public class HttpService implements JavaDelegate {
	FixedValue url;
	FixedValue httpMethod;
	FixedValue requestBody;

	@Override
	public void execute(DelegateExecution execution) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(10 * 1000);
		requestFactory.setReadTimeout(10 * 1000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		String url = this.url.getValue(execution).toString();
		HttpMethod method = HttpMethod.valueOf(this.httpMethod.getValue(execution).toString());
		switch (method) {
		case POST:
			String requestBody = this.requestBody.getValue(execution).toString();
			Class responseType = String.class;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, responseType);
			break;
		case GET:

			break;

		default:
			break;
		}

	}

}
