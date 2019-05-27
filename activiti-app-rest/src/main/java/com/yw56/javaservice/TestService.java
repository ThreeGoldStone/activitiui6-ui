package com.yw56.javaservice;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestService implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(TestService.class);
	private FixedValue  field2;
	private FixedValue  fieldName;
	@Override
	public void execute(DelegateExecution execution) {
		Map<String, Object> variablesLocal = execution.getVariablesLocal();
		log.info("local variables  =  " + variablesLocal);
		Map<String, Object> variables = execution.getVariables();
		String expressionText = field2.getExpressionText();
		String expressionText2 = fieldName.getExpressionText();
		Object value = field2.getValue(execution);
		Object value2 = fieldName.getValue(execution);
		log.info("variables  =  " + variables);
		Map<String, VariableInstance> variableInstances = execution.getVariableInstances();
	}

}
