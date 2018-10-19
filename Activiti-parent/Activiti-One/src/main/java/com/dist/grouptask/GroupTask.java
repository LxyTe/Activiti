package com.dist.grouptask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class GroupTask {

	
	ProcessEngine processEngine=	ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义*/
	@Test
	public void deployProcess() {
	  InputStream inputStreambpmn=	this.getClass().getResourceAsStream("grouptask.bpmn");
	     InputStream inputStreampng =  this.getClass().getResourceAsStream("grouptask.png");       
	Deployment deployment=  processEngine.getRepositoryService().createDeployment()
	                                      .name("组任务")
	                                      .addInputStream("grouptask.bpmn", inputStreambpmn)
	                                      .addInputStream("grouptask.png", inputStreampng)
	                                      .deploy();
	  System.out.println("流程定义id:"+deployment.getId());
	  System.out.println("部署名字:"+deployment.getName());
	}
	/**启动,使用流程变量指定任务办理人*/
	@Test
	public void start() {
//		Map<String, Object>map=new HashMap<String, Object>();
//		map.put("userName", "令狐冲,大大,小小");可实现以流程变量加载任务组的方式，主要是用,来区分
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
		.startProcessInstanceByKey("grouptaskProcess");
		System.out.println("流程id:"+processInstance.getId());
		System.out.println("流程实例id:"+processInstance.getProcessInstanceId());
	}
	/**个人组任务查询*/
	@Test
	public void findPersonalTask() {
	 Task task=	processEngine.getTaskService()
		             .createTaskQuery()
		             .taskCandidateUser("A")//查询组任务
		             //.taskAssignee("1616")
		             .singleResult();
	 System.out.println(task.getId());
	 System.out.println(task.getProcessInstanceId());
	 System.out.println(task.getAssignee());
	 System.out.println(task.getCreateTime());
	}
	/**完成*/
	@Test	
	public void complate() {
		processEngine.getTaskService().complete("152504");
		System.out.println(true);
	}
	/**删除*/
	@Test
	public void deleteProcess() {
		processEngine.getRepositoryService().deleteDeployment("167501",true);
	}

	
	/**查询正在执行的任务办理人表 act_ru_identitylink */
	@Test
	public void findRunPersonTask() {
	List<IdentityLink> tt = processEngine.getTaskService()
		             .getIdentityLinksForTask("152504");
	for (IdentityLink identityLink : tt) {
		System.out.println(identityLink.getUserId());
	}
	}
	/**查询历史组任务表 act_hi_identitylink*/
	@Test
	public void findHistoryPersonTask() {
		List<HistoricIdentityLink> tt= processEngine.getHistoryService()
		              .getHistoricIdentityLinksForTask("152504");
	    for (HistoricIdentityLink historicIdentityLink : tt) {
			System.out.println(historicIdentityLink.getUserId());
			System.out.println(historicIdentityLink.getType());
			System.out.println(historicIdentityLink.getTaskId());
		}
	}
	/** 拾取任务 分配组任务给个人，和换人处理，原理基本一致*/
	@Test
	public void claim() {
		processEngine.getTaskService().claim("172504", "小B");
		// processEngine.getTaskService().setAssignee("172504", null);//返回给组任务
	}
	/**向组中添加成员*/
	@Test
	public void addUserGroup() {
		processEngine.getTaskService().addCandidateUser("152504", "新仔");
	
	}
	/**从组中删除成员*/
	@Test
	public void deleteUserGroup() {
		processEngine.getTaskService().deleteCandidateUser("152504", "B");
	}
}
