package com.dist.personaltask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class PersonalTask {

	
	ProcessEngine processEngine=	ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义*/
	@Test
	public void deployProcess() {
	  InputStream inputStreambpmn=	this.getClass().getResourceAsStream("PersonalTask2.bpmn");
	     InputStream inputStreampng =  this.getClass().getResourceAsStream("PersonalTask2.png");       
	Deployment deployment=  processEngine.getRepositoryService().createDeployment()
	                                      .name("个人任务类加载2")
	                                      .addInputStream("PersonalTask2.bpmn", inputStreambpmn)
	                                      .addInputStream("PersonalTask2.png", inputStreampng)
	                                      .deploy();
	  System.out.println("流程定义id:"+deployment.getId());
	  System.out.println("部署名字:"+deployment.getName());
	}
	/**启动,使用流程变量指定任务办理人*/
	@Test
	public void start() {
//		Map<String, Object>map=new HashMap<String, Object>();
//		map.put("userName", "令狐冲");
//	ProcessInstance processInstance=	processEngine.getRuntimeService()
//		.startProcessInstanceByKey("PersonalTask",map);
		
		//使用类加载办理人
		/**
		 * <extensionElements>
        <activiti:taskListener event="create" class="com.dist.personaltask.PersonListenerImpl"></activiti:taskListener>
          </extensionElements>
		 * 
		 * */
		ProcessInstance processInstance=	processEngine.getRuntimeService()
		.startProcessInstanceByKey("PersonalTask2");
		System.out.println("流程id:"+processInstance.getId());
		System.out.println("流程实例id:"+processInstance.getProcessInstanceId());
	}
	/**个人任务查询*/
	@Test
	public void findPersonalTask() {
	 Task task=	processEngine.getTaskService()
		             .createTaskQuery()
		             .taskAssignee("独孤九剑")
		             .singleResult();
	 System.out.println(task.getId());
	 System.out.println(task.getProcessInstanceId());
	 System.out.println(task.getAssignee());
	 System.out.println(task.getCreateTime());
	}
	/**完成*/
	@Test	
	public void complate() {
		processEngine.getTaskService().complete("137504");
		System.out.println(true);
	}
	/**移交任务给另一个人*/
	@Test
	public void setAssigneeTask() {
		  processEngine.getTaskService().setAssignee("137504", "扫地僧");
	}
}
