import org.activiti.dmn.engine.DmnEngineConfiguration;
import org.activiti.dmn.engine.configurator.DmnEngineConfigurator;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.form.engine.FormEngineConfiguration;
import org.activiti.form.engine.configurator.FormEngineConfigurator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.managed.BasicManagedDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Test {
    @org.junit.Test
    public void test() {
//        ProcessEngineConfigurationImpl processEngineConfiguration = processEngineConfiguration();
//        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        ProcessEngine   processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("org.postgresql.Driver")
                .setJdbcUrl("jdbc:postgresql://172.16.56.131:5432/activiti6")
                .setJdbcPassword("123")
                .setJdbcUsername("djl")
                .setAsyncExecutorActivate(true)
                .buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testExtjs2");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessInstanceId());
        try {
            Thread.sleep(80000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //  @Bean(name = "processEngine")
//    public ProcessEngineFactoryBean processEngineFactoryBean() {
//        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
//        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
//        return factoryBean;
//    }
//
//    public ProcessEngine processEngine() {
//        // Safe to call the getObject() on the @Bean annotated processEngineFactoryBean(), will be
//        // the fully initialized object instanced from the factory and will NOT be created more than once
//        try {
//            return processEngineFactoryBean().getObject();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    //  @Bean(name = "processEngineConfiguration")
    public ProcessEngineConfigurationImpl processEngineConfiguration() {
        StandaloneProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        BasicDataSource basicDataSource = new BasicManagedDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl("jdbc:postgresql://172.16.56.131:5432/activiti6");
        basicDataSource.setUsername("djl");
        basicDataSource.setPassword("123");
        processEngineConfiguration.setDataSource(basicDataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setTransactionFactory(new JdbcTransactionFactory());
        processEngineConfiguration.setAsyncExecutorActivate(true);
        processEngineConfiguration.setAsyncExecutor(asyncExecutor());

//        String emailHost = environment.getProperty("email.host");
//        if (StringUtils.isNotEmpty(emailHost)) {
//            processEngineConfiguration.setMailServerHost(emailHost);
//            processEngineConfiguration.setMailServerPort(environment.getRequiredProperty("email.port", Integer.class));
//
//            Boolean useCredentials = environment.getProperty("email.useCredentials", Boolean.class);
//            if (Boolean.TRUE.equals(useCredentials)) {
//                processEngineConfiguration.setMailServerUsername(environment.getProperty("email.username"));
//                processEngineConfiguration.setMailServerPassword(environment.getProperty("email.password"));
//            }
//
//            Boolean emailSSL = environment.getProperty("email.ssl", Boolean.class);
//            if (emailSSL != null) {
//                processEngineConfiguration.setMailServerUseSSL(emailSSL.booleanValue());
//            }
//
//            Boolean emailTLS = environment.getProperty("email.tls", Boolean.class);
//            if (emailTLS != null) {
//                processEngineConfiguration.setMailServerUseTLS(emailTLS.booleanValue());
//            }
//        }

        // Limit process definition cache
//        processEngineConfiguration.setProcessDefinitionCacheLimit(environment.getProperty("activiti.process-definitions.cache.max", Integer.class, 128));
//
//        // Enable safe XML. See http://activiti.org/userguide/index.html#advanced.safe.bpmn.xml
//        processEngineConfiguration.setEnableSafeBpmnXml(true);
//
//        List<BpmnParseHandler> preParseHandlers = new ArrayList<BpmnParseHandler>();
//        processEngineConfiguration.setPreBpmnParseHandlers(preParseHandlers);
//
//        FormEngineConfiguration formEngineConfiguration = new FormEngineConfiguration();
//        formEngineConfiguration.setDataSource(dataSource);
//
//        FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
//        formEngineConfigurator.setFormEngineConfiguration(formEngineConfiguration);
//        processEngineConfiguration.addConfigurator(formEngineConfigurator);
//
//        DmnEngineConfiguration dmnEngineConfiguration = new DmnEngineConfiguration();
//        dmnEngineConfiguration.setDataSource(dataSource);
//
//        DmnEngineConfigurator dmnEngineConfigurator = new DmnEngineConfigurator();
//        dmnEngineConfigurator.setDmnEngineConfiguration(dmnEngineConfiguration);
//        processEngineConfiguration.addConfigurator(dmnEngineConfigurator);

        return processEngineConfiguration;
    }

    //    @Bean
    public AsyncExecutor asyncExecutor() {
        DefaultAsyncJobExecutor asyncExecutor = new DefaultAsyncJobExecutor();
        asyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(5000);
        asyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(5000);
        asyncExecutor.setAutoActivate(true);
        return asyncExecutor;
    }
}
