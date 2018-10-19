package com.dist.receivetask;
/**
 * 接收任务活动 
 * @author Administrator
 *
 */

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {
    
	
	
	ProcessEngine processEngine = 	ProcessEngines.getDefaultProcessEngine();
	
	 @Test
	 public void deploymentProcessDefinition() {
       InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
	   InputStream inputStreamPng =  this.getClass().getResourceAsStream("receiveTask.png");
	Deployment deployment =   processEngine.getRepositoryService()
		              .createDeployment()
		              .name("接收等待任务")
		              .addInputStream("receiveTask.bpmn", inputStreamBpmn)
		              .addInputStream("receiveTask.png", inputStreamPng)
		              .deploy();
       System.out.println("部署id:"+deployment.getId());   
       System.out.println("部署Name:"+deployment.getName());             
	 }

	 @Test
	 public void startProcessInstance() {
	ProcessInstance processInstance =	 processEngine.getRuntimeService()
		              .startProcessInstanceByKey("receiveTask");
	   System.out.println("流程实例id:"+processInstance.getId());
	   System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	
	   /**查询执行对象  ，  因为用的是延时(接收)任务,所以无法使用taskServie*/
	  Execution execution= processEngine.getRuntimeService()
	                  .createExecutionQuery()//创建执行对象查询，
	                  .processInstanceId(processInstance.getId())
	                  .singleResult();
	 
	  /**使用流程变量设置当日销售额，用来传递参数*/
	  processEngine.getRuntimeService().setVariable(execution.getId(), "当日销售额", 9527);
	
	  /**向后执行一步,可以使处于等待状态的流程，往后继续走*/
	  processEngine.getRuntimeService().signal(execution.getId());
	  
	  /**查询执行对象id,现在已经走到通知管理人流程*/
	  Execution execution2= processEngine.getRuntimeService().createExecutionQuery()
	               .processInstanceId(processInstance.getId())
	                .singleResult();
	 
	  /**获取流程变量中的值*/
	Integer data=(Integer)  processEngine.getRuntimeService().getVariable(execution2.getId(), "当日销售额");

	System.out.println("从流程变量中获取的销售额:"+data);
	
	/**继续执行完流程*/
	processEngine.getRuntimeService().signal(execution2.getId());
	 }
	
}
