import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname MainTest
 * @Description TODO
 * @Date 2019/6/24 14:50
 * @Created by djl20
 */
public class MainTest_Exception {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("org.postgresql.Driver")
                .setJdbcUrl("jdbc:postgresql://172.16.56.131:5432/activiti6")
                .setJdbcPassword("123")
                .setJdbcUsername("djl")
                .setAsyncExecutorActivate(true)
                .buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        deploy(repositoryService);
//        startProcess(runtimeService);


        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processDefinitionKey("test-handTask").processInstanceId("190005").singleResult();
        System.out.println(instance);
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processInstanceId("102514").list();
        System.out.println(list);
//        try {
//            Thread.sleep(80000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private static void deploy(RepositoryService repositoryService) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list);
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("bpm/test-handTask2.bpmn20.xml")
                .deploy();
        System.out.println(deploy.getKey());
        List<ProcessDefinition> list2 = repositoryService.createProcessDefinitionQuery().active().list();
        System.out.println(list2);
    }

    private static void startProcess(RuntimeService runtimeService) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("exc", "抛错把");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test-handTask", vars);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessInstanceId());
    }
}
