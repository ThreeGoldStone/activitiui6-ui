/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.app.rest.editor;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.app.domain.editor.AppDefinition;
import org.activiti.app.domain.editor.AppModelDefinition;
import org.activiti.app.domain.editor.Model;
import org.activiti.app.model.editor.AppDefinitionPublishRepresentation;
import org.activiti.app.model.editor.AppDefinitionUpdateResultRepresentation;
import org.activiti.app.model.editor.ModelRepresentation;
import org.activiti.app.repository.editor.ModelRepository;
import org.activiti.app.security.SecurityUtils;
import org.activiti.app.service.api.ModelService;
import org.activiti.app.service.editor.AppDefinitionImportService;
import org.activiti.app.service.exception.InternalServerErrorException;
import org.activiti.app.service.runtime.ActivitiService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yw56.javaservice.JavaServiceEntity;

import javax.inject.Inject;

/**
 * @author djl20
 */
@RestController
public class JavaServicesResource {

    private final Logger log = LoggerFactory.getLogger(JavaServicesResource.class);
    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected RuntimeService runtimeService;

    @Inject
    protected ModelService modelService;
    //
    @Inject
    protected ObjectMapper objectMapper;
    @Autowired
    protected ActivitiService activitiService;
    @Autowired
    protected ModelRepository modelRepository;
    @Autowired
    protected AppDefinitionImportService appDefinitionImportService;

    @RequestMapping(value = "/rest/java/services", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<JavaServiceEntity> getJavaserviceList() {
//		try {
//			String fileName = "java-services.js";
//			JsonNode stencilNode = objectMapper
//					.readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//			return stencilNode;
//		} catch (Exception e) {
//			log.error("Error reading bpmn stencil set json", e);
//			throw new InternalServerErrorException("Error reading bpmn stencil set json");
//		}
        return JavaServiceEntity.getData();
    }

    @RequestMapping(value = "/rest/java/ExtServices", method = RequestMethod.GET, produces = "application/json")
    public JsonNode getExtJsServiceList() {
        try {
            String fileName = "extjsAppList.json";
            JsonNode stencilNode = objectMapper
                    .readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
            return stencilNode;
        } catch (Exception e) {
            log.error("Error reading extjsAppList.json", e);
            throw new InternalServerErrorException("Error reading extjsAppList.json json");
        }
    }

    @RequestMapping(value = "/rest/java/service/details/{serviceid}", method = RequestMethod.GET, produces = "application/json")
    public JsonNode getExtJsServiceDetail(@PathVariable String serviceid) {
        System.out.println("getExtJsServiceDetail > " + serviceid);
        try {
            String fileName = "detail.json";
            ObjectNode stencilNode = (ObjectNode) objectMapper
                    .readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
            try {
                String fileNameReq = serviceid + "/request.json";
                JsonNode req = objectMapper
                        .readTree(this.getClass().getClassLoader().getResourceAsStream(fileNameReq));
                String fileNameRes = serviceid + "/response.json";
                JsonNode rep = objectMapper
                        .readTree(this.getClass().getClassLoader().getResourceAsStream(fileNameRes));
                stencilNode.put("requestTemplate", req.toString());
                stencilNode.put("responceTemplate", rep.toString());
            } catch (Exception e) {
            }
            return stencilNode;
        } catch (Exception e) {
            log.error("Error reading detail.json", e);
            throw new InternalServerErrorException("Error reading detail.json json");
        }
    }

    @RequestMapping(value = "/rest/java/getPDs", method = RequestMethod.GET, produces = "application/json")
    public List<ProcessDefinition> getProcessDefines() {
//		try {
//			String fileName = "java-services.js";
//			JsonNode stencilNode = objectMapper
//					.readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//			return stencilNode;
//		} catch (Exception e) {
//			log.error("Error reading bpmn stencil set json", e);
//			throw new InternalServerErrorException("Error reading bpmn stencil set json");
//		}
//        repositoryService.ge
//        runtimeService
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

        return list;
    }

    @RequestMapping(value = "/rest/java/startProcessInstance", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String startProcessInstance(@RequestBody JSONObject jsonObject) {
//		try {
//			String fileName = "java-services.js";
//			JsonNode stencilNode = objectMapper
//					.readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//			return stencilNode;
//		} catch (Exception e) {
//			log.error("Error reading bpmn stencil set json", e);
//			throw new InternalServerErrorException("Error reading bpmn stencil set json");
//		}
//        repositoryService.ge
//        runtimeService
        log.info(jsonObject.toJSONString());
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        String processId = jsonObject.getString("processId");
        String processInstanceName = jsonObject.getString("processInstanceName");
        JSONObject variables = jsonObject.getJSONObject("variables");
        ProcessInstance processInstance = activitiService.startProcessInstanceByKey(processId, variables, processInstanceName);

        return "success";
    }

    @RequestMapping(value = "/rest/java/saveEditDefAndPublish/{modelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public AppDefinitionUpdateResultRepresentation saveAndPublish(@PathVariable String modelId) {
        Model modelProcessDef = modelService.getModel(modelId);
        String processDefKey = modelProcessDef.getKey();
        // 先查询有没有modelId对应的app
        List<Model> apps = modelRepository.findModelsByKeyAndType(processDefKey + "xx-xx-app", Model.MODEL_TYPE_APP);
        Model appModel = null;
        if (apps.size() == 0) {
            // 没有就新建一个
            ModelRepresentation modelRepresentation = new ModelRepresentation();
            modelRepresentation.setKey(processDefKey + "xx-xx-app");
            modelRepresentation.setName(processDefKey + "xx-xx-app");
            modelRepresentation.setDescription("自动创建的发布app");
            modelRepresentation.setModelType(Model.MODEL_TYPE_APP);

            String json = null;
            try {
                AppDefinition appDefinition = new AppDefinition();
                appDefinition.setIcon("glyphicon-asterisk");
                appDefinition.setTheme("theme-1");
                List<AppModelDefinition> models = new ArrayList<>();
                AppModelDefinition appModelDefinition = new AppModelDefinition();
                appModelDefinition.setCreatedBy(modelProcessDef.getCreatedBy());
                appModelDefinition.setId(modelProcessDef.getId());
                appModelDefinition.setLastUpdatedBy(modelProcessDef.getLastUpdatedBy());
                appModelDefinition.setName(modelProcessDef.getName());
                appModelDefinition.setLastUpdated(modelProcessDef.getLastUpdated());
                appModelDefinition.setVersion(modelProcessDef.getVersion());
                appModelDefinition.setModelType(modelProcessDef.getModelType());
                models.add(appModelDefinition);
                appDefinition.setModels(models);

                json = objectMapper.writeValueAsString(appDefinition);
            } catch (Exception e) {
                log.error("Error creating app definition", e);
                throw new InternalServerErrorException("Error creating app definition");
            }
            appModel = modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());
        } else {
            appModel = apps.get(0);
        }
        // 发布app
        AppDefinitionPublishRepresentation publishModel = new AppDefinitionPublishRepresentation();
        return appDefinitionImportService.publishAppDefinition(appModel.getId(), publishModel);
//
//
//        // 有就
//
//        String json = null;
//        try {
//            json = objectMapper.writeValueAsString(new AppDefinition());
//        } catch (Exception e) {
//            log.error("Error creating app definition", e);
//            throw new InternalServerErrorException("Error creating app definition");
//        }
//
//        // 创建新的app
//        ModelRepresentation modelRepresentation = new ModelRepresentation();
//
//        Model newModel = modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());
//        modelRepository.findModelsByKeyAndType()
//        // 更新app
//        Model model = modelService.getModel(modelId);
//
//        model.setName(updatedModel.getAppDefinition().getName());
//        model.setKey(updatedModel.getAppDefinition().getKey());
//        model.setDescription(updatedModel.getAppDefinition().getDescription());
//        String editorJson = null;
//        try {
//            editorJson = objectMapper.writeValueAsString(updatedModel.getAppDefinition().getDefinition());
//        } catch (Exception e) {
//            logger.error("Error while processing app definition json " + modelId, e);
//            throw new InternalServerErrorException("App definition could not be saved " + modelId);
//        }
//
//        model = modelService.saveModel(model, editorJson, null, false, null, user);
//        return appDefinitionImportService.publishAppDefinition(modelId, new AppDefinitionPublishRepresentation(null, updatedModel.getForce()));
//        // 发布app
//

    }
}
