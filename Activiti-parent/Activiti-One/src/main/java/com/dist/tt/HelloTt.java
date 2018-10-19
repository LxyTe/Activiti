package com.dist.tt;

import java.util.List;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloTt {
	
	//得到默认的流程引擎对象	，连接的时候默认读取activiti.cfg.xml的值，认文件名字的
	  ProcessEngine processEngine =	 ProcessEngines.getDefaultProcessEngine();
	  
    /*** 部署流程定义*/	
//	@Test
//	public void deploy() {
//      Deployment deployment=  processEngine.getRepositoryService()//流程定义和部署对象相关的service
//			   .createDeployment()//创建一个部署对象
//			   .name("tt")//添加部署名称
//			   .addClasspathResource("diagrams/tt.bpmn")
//			   .addClasspathResource("diagrams/tt.png")
//			   .deploy();// 完成部署	   
//       System.out.println("部署id"+deployment.getId());
//       System.out.println(deployment.getName());
//	}
//	
//	/**启动流程实例*/
	@Test
	public void startProcessEngine() {
	ProcessInstance processInstance=	 processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
		             .startProcessInstanceByKey("tt"); //使用流程定义的key启动流程实施，key对应的值,使用key启动，默认启动的是最新版本
		System.out.println("流程实例id:"+processInstance.getId());
		System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	}
	/**查询当前个人的任务*/
	@Test
	public void findMyPersonalTask() {
	List<Task> list =	processEngine.getTaskService()//与正在执行的任务管理相关的service
		             .createTaskQuery() //创建查询对象
		             .taskAssignee("张三")//指定确切的某个人
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
//	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask() {
      //任务id
		processEngine.getTaskService().complete("15002");
	 System.out.println("10008流程已经走完");
	}
	
}
