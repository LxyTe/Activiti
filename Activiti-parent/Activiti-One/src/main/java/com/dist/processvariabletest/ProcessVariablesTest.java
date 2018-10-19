package com.dist.processvariabletest;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.h2.util.New;
import org.junit.Test;

public class ProcessVariablesTest {

	 ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
	
	 /**部署流程定义*/
	 @Test
	 public void deploymentProcessDefinition() {
//		InputStream inputStream=  this.getClass().getClassLoader().getResourceAsStream("diagrams/tt.zip");
//		 ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			Deployment deployment=	 processEngine.getRepositoryService()
				                         .createDeployment() //创建一个部署对象
				                         .name("MM2流程")
				                         .addClasspathResource("diagrams/processVariables.bpmn")
				                         .addClasspathResource("diagrams/processVariables.png")
				                         .deploy();
				              System.out.println("部署id:"+deployment.getId());   
				              System.out.println("部署Name:"+deployment.getName());  
	 }
	 /**启动流程实例*/
	 @Test
	 public void startProcessInstance() {
	ProcessInstance processInstance =	 processEngine.getRuntimeService()
		              .startProcessInstanceByKey("processVariables");
	   System.out.println("流程实例id:"+processInstance.getId());
	   System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	 }
	 /**设置流程变量*/
	 @Test
	 public void setVariables() {
	  TaskService taskService=	 processEngine.getTaskService();
//	   taskService.setVariable("27506", "请假时间", 3);
//	   taskService.setVariable("27506", "请假原因", "回家玩耍");
//	   taskService.setVariable("27506", "请假开始时间", new Date());//此方法表示与任务id绑定,但是，当此流程走到下一个人的时候，使用VariableLocal的变量，会丢失，因为id被改变了
	    //以javaBean的形式注入对象信息
	  taskService.setVariableLocal("35004", "请假变量信息", new Person(5, "lxy"));
	   System.out.println(true);
	 }
	 
	 /**获取流程变量*/
	 @Test
	 public void getVariables() {
		TaskService taskService=   processEngine.getTaskService();
	
//		String ss= (String)  taskService.getVariable("27506", "请假原因");//流程在第一个人走完之后，对应的id也会做改变
//		Date date= (Date)   taskService.getVariable("27506", "请假开始时间");
//		        Integer day = (Integer) taskService.getVariable("27506", "请假时间");
//		 System.out.println(ss);
//		 System.out.println(day);
//		 System.out.println(date);
		//获取整个javaBean的信息
		 Person pp = (Person) taskService.getVariable("35004", "请假变量信息");
		 System.out.println(pp);
	 }
	 /**模拟 设置和获取流程变量的场景*/
	 public void setAndGetVariables() {
		 /**与流程实例，执行对象相关*/
	  RuntimeService runtimeService =	 processEngine.getRuntimeService();
	
	    /**与任务 正在执行相关*/
	 TaskService taskService =  processEngine.getTaskService();
	
	  /**设置流程变量*/
	  // runtimeService.setVariable(executionId, variableName, value); 3个参数依次为 执行对象id，变量名称,变量值,只能设置一个
	   //  runtimeService.setVariables(executionId, variables);第二个参数是一个man集合，可以设置多个变量和值
	 
	  // taskService.setVariable(taskid, variableName, value); 3个参数依次为 任务id，变量名称,变量值,只能设置一个
	   //  taskService.setVariables(taskid, variables);第二个参数是一个man集合，可以设置多个变量和值
	  
	 //runtimeService.startProcessInstanceByKey(processDefinitionKey, variables),启动流程实例的也可以用map集合来创建，完成也可以
	
	 /**获取 流程变量*/
	// runtimeService.getVariables(executionId) //获取所有的流程变量，。。。。
	 //runtimeService.getVariables(executionId, variableNames) 通过设置流程变量名称获取值，使用map集合的形式，可以指定多个名字
	 /**taskService同理*/
	 }
	 /**完成任务*/
	 @Test
	 public void completeMyPersonalTask() {
		  processEngine.getTaskService().complete("40004"); 
		  System.out.println(true);
	 }
	 /**查询历史流程变量*/
	 @Test
	 public void findHistoryVariable() {
	  HistoricVariableInstance historicVariableInstance  =	 processEngine.getHistoryService()
		              .createHistoricVariableInstanceQuery()
		              .variableName("请假变量信息")
		              .singleResult();
	  System.out.println(historicVariableInstance.getVariableName());
	  System.out.println(historicVariableInstance.getValue());
	  System.out.println(true);
	 }
}
