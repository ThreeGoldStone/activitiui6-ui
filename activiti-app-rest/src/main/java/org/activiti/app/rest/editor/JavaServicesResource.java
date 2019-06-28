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
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yw56.javaservice.EncoderHandler;
import com.yw56.javaservice.PropertiesUtils;
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
import org.activiti.engine.impl.asyncexecutor.AsyncExecutor;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.yw56.javaservice.JavaServiceEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

import static com.yw56.javaservice.PropertiesUtils.extProperties;

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
    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    private String extUrl = extProperties.getProperty("extjs.url");
    private String extToken = extProperties.getProperty("extjs.token");

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

    @RequestMapping(value = "/rest/java/ExtServices/{tenantid}", method = RequestMethod.GET, produces = "application/json")
    public JSONArray getExtJsServiceList(@PathVariable String tenantid) {
//        try {
//            String fileName = "extjsAppList.json";
//            JsonNode stencilNode = objectMapper
//                    .readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//            return stencilNode;
//        } catch (Exception e) {
//            log.error("Error reading extjsAppList.json", e);
//            throw new InternalServerErrorException("Error reading extjsAppList.json json");
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "getTenantSubServices");
        jsonObject.put("tenantid", tenantid);
        String dataS = postExtJs(jsonObject.toJSONString());
        JSONObject result = JSON.parseObject(dataS);
        JSONArray data = result.getJSONArray("data");
        JSONArray array = new JSONArray();
        JSONObject appTmps = new JSONObject();
        for (int i = 0; i < data.size(); i++) {
            JSONObject o = data.getJSONObject(i);
            String prodid = o.getString("prodid");
            JSONObject appObj = appTmps.getJSONObject(prodid);

            if (appObj == null) {
                appObj = new JSONObject();
                appObj.put("id", o.get("prodid"));
                appObj.put("appname", o.get("prodname"));
                JSONArray serviceArray = new JSONArray();
                appObj.put("serviceList", serviceArray);
                appTmps.put(prodid, appObj);
            }
            JSONArray serviceList = appObj.getJSONArray("serviceList");
            JSONObject subO = new JSONObject();
            subO.put("serviceid", o.get("serviceid"));
            subO.put("servicename", o.get("servicename"));
            subO.put("servicedesc", o.get("servicedesc"));
            serviceList.add(subO);
        }
        for (Map.Entry<String, Object> entry : appTmps.entrySet()) {
            array.add(entry.getValue());
        }
        String s = array.toJSONString();
        return array;
    }

    @RequestMapping(value = "/rest/java/service/details/{serviceid}", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getExtJsServiceDetail(@PathVariable String serviceid) {
        System.out.println("getExtJsServiceDetail > " + serviceid);
//        try {
//            String fileName = "detail.json";
//            ObjectNode stencilNode = (ObjectNode) objectMapper
//                    .readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//            try {
//                String fileNameReq = serviceid + "/request.json";
//                JsonNode req = objectMapper
//                        .readTree(this.getClass().getClassLoader().getResourceAsStream(fileNameReq));
//                String fileNameRes = serviceid + "/response.json";
//                JsonNode rep = objectMapper
//                        .readTree(this.getClass().getClassLoader().getResourceAsStream(fileNameRes));
//                stencilNode.put("requestTemplate", req.toString());
//                stencilNode.put("responceTemplate", rep.toString());
//            } catch (Exception e) {
//            }
//            return stencilNode;
//        } catch (Exception e) {
//            log.error("Error reading detail.json", e);
//            throw new InternalServerErrorException("Error reading detail.json json");
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "getServiceParamsBody");
        jsonObject.put("serviceid", serviceid);
        String data = postExtJs(jsonObject.toJSONString());
        JSONObject jsonObject1 = JSON.parseObject(data);
        JSONObject data1 = jsonObject1.getJSONObject("data");
        return data1;
    }


    @RequestMapping(value = "/rest/java/startProcessInstance", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String startProcessInstance(@RequestBody JSONObject jsonObject) {
        AsyncExecutor asyncExecutor = processEngineConfiguration.getAsyncExecutor();
        asyncExecutor.start();
        log.info(jsonObject.toJSONString());
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

    @RequestMapping(value = "/rest/java/getExtraParams/{modelId}", method = RequestMethod.GET, produces = "application/json")
    public String getExtraParams(@PathVariable String modelId) {
//        try {
//            String fileName = "extraparams.json";
//            JsonNode stencilNode = objectMapper
//                    .readTree(this.getClass().getClassLoader().getResourceAsStream(fileName));
//            return stencilNode;
//        } catch (Exception e) {
//            log.error("Error reading extarparams.json", e);
//            throw new InternalServerErrorException("Error reading extarparams.json json");
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "getWorkFlowStartBody");
        jsonObject.put("processdefid", modelId);
        return postExtJs(jsonObject.toJSONString());
    }

    private String postExtJs(String json) {
        RestTemplate restTemplate = new RestTemplate(PropertiesUtils.requestFactory);
        HttpMethod method = HttpMethod.POST;
        Class responseType = String.class;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> signature = EncoderHandler.getSignature(extToken);
        for (Map.Entry<String, Object> entry : signature.entrySet()) {
            headers.set(entry.getKey(), entry.getValue().toString());
        }
        HttpEntity requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity responseEntity = restTemplate.exchange(extUrl, method, requestEntity, responseType);
        String data = responseEntity.getBody().toString();
        return data;
    }
}
