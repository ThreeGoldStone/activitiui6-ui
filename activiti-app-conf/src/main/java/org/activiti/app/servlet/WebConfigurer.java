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
package org.activiti.app.servlet;

import com.yw56.javaservice.ClassAccessUtils;
import com.yw56.javaservice.EXTJSServiceJsonConverter;
import org.activiti.app.conf.ApplicationConfiguration;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.yw56.javaservice.EXTJSConstants.STENCIL_EXTJS_TASK_SERVICE;
import static org.activiti.editor.constants.StencilConstants.STENCIL_TASK_SERVICE;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
public class WebConfigurer implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    public AnnotationConfigWebApplicationContext context;

    static {
        Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap = ClassAccessUtils.getStaticPropertyAccess(BpmnJsonConverter.class, "convertersToJsonMap");
        Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap = ClassAccessUtils.getStaticPropertyAccess(BpmnJsonConverter.class, "convertersToBpmnMap");
        // json解析器
        EXTJSServiceJsonConverter.fillTypes(convertersToBpmnMap, convertersToJsonMap);
        List<String> DI_RECTANGLES = ClassAccessUtils.getStaticPropertyAccess(BpmnJsonConverter.class, "DI_RECTANGLES");
        // 添加图像解析类型
        DI_RECTANGLES.add(STENCIL_EXTJS_TASK_SERVICE);
        log.info("BaseBpmnJsonConverter  添加EXTJS服务的JSON解析器");

    }

    public void setContext(AnnotationConfigWebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("Configuring Spring root application context");

        ServletContext servletContext = sce.getServletContext();

        AnnotationConfigWebApplicationContext rootContext = null;

        if (context == null) {
            rootContext = new AnnotationConfigWebApplicationContext();
            rootContext.register(ApplicationConfiguration.class);

            if (rootContext.getServletContext() == null) {
                rootContext.setServletContext(servletContext);
            }

            rootContext.refresh();
            context = rootContext;

        } else {
            rootContext = context;
            if (rootContext.getServletContext() == null) {
                rootContext.setServletContext(servletContext);
            }
        }

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

        initSpring(servletContext, rootContext);
        initSpringSecurity(servletContext, disps);

        log.debug("Web application fully configured");
    }

    /**
     * Initializes Spring and Spring MVC.
     */
    private void initSpring(ServletContext servletContext, AnnotationConfigWebApplicationContext rootContext) {
        log.debug("Configuring Spring Web application context");
        AnnotationConfigWebApplicationContext appDispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        appDispatcherServletConfiguration.setParent(rootContext);
        appDispatcherServletConfiguration.register(AppDispatcherServletConfiguration.class);

        log.debug("Registering Spring MVC Servlet");
        ServletRegistration.Dynamic appDispatcherServlet = servletContext.addServlet("appDispatcher",
                new DispatcherServlet(appDispatcherServletConfiguration));
        appDispatcherServlet.addMapping("/app/*");
        appDispatcherServlet.setLoadOnStartup(1);
        appDispatcherServlet.setAsyncSupported(true);

        log.debug("Registering Activiti public REST API");
        AnnotationConfigWebApplicationContext apiDispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        apiDispatcherServletConfiguration.setParent(rootContext);
        apiDispatcherServletConfiguration.register(ApiDispatcherServletConfiguration.class);

        ServletRegistration.Dynamic apiDispatcherServlet = servletContext.addServlet("apiDispatcher",
                new DispatcherServlet(apiDispatcherServletConfiguration));
        apiDispatcherServlet.addMapping("/api/*");
        apiDispatcherServlet.setLoadOnStartup(1);
        apiDispatcherServlet.setAsyncSupported(true);
    }

    /**
     * Initializes Spring Security.
     */
    private void initSpringSecurity(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering Spring Security Filter");
        FilterRegistration.Dynamic springSecurityFilter = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());

        springSecurityFilter.addMappingForUrlPatterns(disps, false, "/*");
        springSecurityFilter.setAsyncSupported(true);
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Destroying Web application");
        WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        AnnotationConfigWebApplicationContext gwac = (AnnotationConfigWebApplicationContext) ac;
        gwac.close();
        log.debug("Web application destroyed");
    }
}
