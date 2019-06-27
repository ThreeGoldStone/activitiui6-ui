import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

/**
 * @Classname MainTest
 * @Description TODO
 * @Date 2019/6/24 14:50
 * @Created by djl20
 */
public class MainTest {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
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
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpm/test-handTask2.bpmn20.xml")
                .deploy();
        System.out.println(deploy.getKey());
        List<ProcessDefinition> list2 = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list2);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test-handTask");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessInstanceId());
//        try {
//            Thread.sleep(80000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
