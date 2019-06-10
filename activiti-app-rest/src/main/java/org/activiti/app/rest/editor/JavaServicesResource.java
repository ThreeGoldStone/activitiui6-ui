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

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yw56.javaservice.JavaServiceEntity;

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

//
//	@Inject
//	protected ObjectMapper objectMapper;

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
    public ArrayList<JavaServiceEntity> getExtJsServiceList() {
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
}
