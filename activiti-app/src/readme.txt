单独打开一个编辑页面
http://localhost:8080/activiti-app/editor/#/editor/cef74930-1596-4968-b452-bd522dc23397?singleEdit=true
// 预览
background-image: url("/activiti-app/app/rest/models/cef74930-1596-4968-b452-bd522dc23397/thumbnail");
// 创建app
ModelsResource.createModel


// 更新保存app
org.activiti.app.rest.editor.AppDefinitionResource.updateAppDefinition
// 发布
。。。publishAppDefinition


//获取编辑列表
http://localhost:8080/activiti-app/app/rest/models?filter=myProcesses&modelType=0&sort=modifiedDesc

//获取执行的或执行过的流程列表
http://localhost:8080/activiti-app/app/rest/query/process-instances
