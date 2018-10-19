package com.dist.gateway;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 并行网关
 * *
 * @author Administrator
 *
 */
public class parallelGateWay {

	
	ProcessEngine processEngine = 	ProcessEngines.getDefaultProcessEngine();
	
	 @Test
	 public void deploymentProcessDefinition() {
       InputStream inputStreamBpmn = this.getClass().getResourceAsStream("parallelGateWay.bpmn");
	   InputStream inputStreamPng =  this.getClass().getResourceAsStream("parallelGateWay.bpmn");
	Deployment deployment =   processEngine.getRepositoryService()
		              .createDeployment()
		              .name("并行网关")
		              .addInputStream("parallelGateWay.bpmn", inputStreamBpmn)
		              .addInputStream("parallelGateWay.bpmn", inputStreamPng)
		              .deploy();
       System.out.println("部署id:"+deployment.getId());   
       System.out.println("部署Name:"+deployment.getName());             
	 }

	 @Test
	 public void startProcessInstance() {
	ProcessInstance processInstance =	 processEngine.getRuntimeService()
		              .startProcessInstanceByKey("parallelGateWay");
	   System.out.println("流程实例id:"+processInstance.getId());
	   System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	 }
	 
	 /**执行流程*/
	 @Test
	 public void completeMyPersonalTask() {
		 processEngine.getTaskService().complete("95002");
		 System.out.println(true);
	 }
}
