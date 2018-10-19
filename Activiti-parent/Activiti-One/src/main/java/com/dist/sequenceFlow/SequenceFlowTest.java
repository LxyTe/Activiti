package com.dist.sequenceFlow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class SequenceFlowTest {

	
		 ProcessEngine processEngine=	 ProcessEngines.getDefaultProcessEngine();
	
		 @Test
		 public void deploymentProcessDefinition() {
           InputStream inputStreamBpmn = this.getClass().getResourceAsStream("sequenceFlow.bpmn");
		InputStream inputStreamPng = 	this.getClass().getResourceAsStream("sequenceFlow.png");
		Deployment deployment =   processEngine.getRepositoryService()
			              .createDeployment()
			              .name("连线操作")
			              .addInputStream("sequenceFlow.bpmn", inputStreamBpmn)
			              .addInputStream("sequenceFlow.png", inputStreamPng)
			              .deploy();
           System.out.println("部署id:"+deployment.getId());   
           System.out.println("部署Name:"+deployment.getName());             
		 }
		 
		 /**启动流程实例*/
		 @Test
		 public void startProcessInstance() {
		ProcessInstance processInstance =	 processEngine.getRuntimeService()
			              .startProcessInstanceByKey("sequenceFolw");
		   System.out.println("流程实例id:"+processInstance.getId());
		   System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
		 }
		 
			/**查询当前个人的任务*/
			@Test
			public void findMyPersonalTask() {
			List<Task> list =	processEngine.getTaskService()//与正在执行的任务管理相关的service
				             .createTaskQuery() //创建查询对象
				             .taskAssignee("大大")//指定确切的某个人
				             .list();
			  if(list !=null && list.size()>0) {
				 for (Task task : list) {
					System.out.println("任务id"+task.getId());
					System.out.println("任务名称"+task.getName());
					System.out.println("任务创建时间"+task.getCreateTime());
					System.out.println("任务办理人"+task.getAssignee());
					System.out.println("任务实例id" +task.getProcessInstanceId());
					System.out.println("执行对象id" + task.getExecutionId());
				} 
			  }
			}
			
//			/**完成我的任务*/
			@Test
			public void completeMyPersonalTask() {
		      //任务id
//				Map<String, Object>map=new HashMap<String, Object>();
//				map.put("message", "重要");
				processEngine.getTaskService().complete("60003");
			 System.out.println("流程已经走完");
			}
		 
}
