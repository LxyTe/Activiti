package com.dist.gateway;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 排他网关测试
 * @author Administrator
 *
 */
public class GateWayTest {

	
	ProcessEngine processEngine = 	ProcessEngines.getDefaultProcessEngine();
	
	 @Test
	 public void deploymentProcessDefinition() {
       InputStream inputStreamBpmn = this.getClass().getResourceAsStream("GateWay.bpmn");
	   InputStream inputStreamPng =  this.getClass().getResourceAsStream("GateWay.png");
	Deployment deployment =   processEngine.getRepositoryService()
		              .createDeployment()
		              .name("排他网关")
		              .addInputStream("GateWay.bpmn", inputStreamBpmn)
		              .addInputStream("GateWay.png", inputStreamPng)
		              .deploy();
       System.out.println("部署id:"+deployment.getId());   
       System.out.println("部署Name:"+deployment.getName());             
	 }

	 @Test
	 public void startProcessInstance() {
	ProcessInstance processInstance =	 processEngine.getRuntimeService()
		              .startProcessInstanceByKey("GateWay");
	   System.out.println("流程实例id:"+processInstance.getId());
	   System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	 }
	 
		/**完成我的任务*/
		@Test
		public void completeMyPersonalTask() {
	      //任务id
			Map<String, Object>map=new HashMap<String, Object>();
			map.put("money", 50000);
			processEngine.getTaskService().complete("80005",map);
		 System.out.println("流程已经走完");
		}
}
