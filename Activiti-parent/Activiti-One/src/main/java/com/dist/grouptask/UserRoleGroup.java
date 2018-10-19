package com.dist.grouptask;

import java.io.InputStream;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

/**
 * 用户角色组
 * @author Administrator
 *
 */
public class UserRoleGroup {

	ProcessEngine processEngine=	ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义 userrolegroup*/
	@Test
	public void deployProcess() {
	  InputStream inputStreambpmn=	this.getClass().getResourceAsStream("userrolegroup.bpmn");
	     InputStream inputStreampng =  this.getClass().getResourceAsStream("userrolegroup.png");       
	Deployment deployment=  processEngine.getRepositoryService().createDeployment()
	                                      .name("用户角色组")
	                                      .addInputStream("userrolegroup.bpmn", inputStreambpmn)
	                                      .addInputStream("userrolegroup.png", inputStreampng)
	                                      .deploy();
	  System.out.println("流程定义id:"+deployment.getId());
	  System.out.println("部署名字:"+deployment.getName());
	  
	  /**添加用户角色组*/
	 IdentityService identityService = processEngine.getIdentityService();//用户角色相关service
	  //创建角色
	 identityService.saveGroup(new GroupEntity("部门经理"));
	 identityService.saveGroup(new GroupEntity("总经理"));
	 
	 //创建用户
	 identityService.saveUser(new UserEntity("大A"));
	 identityService.saveUser(new  UserEntity("小B"));
	 identityService.saveUser(new UserEntity("中C"));
	 
	 //建立用户和组之间的关系
	 identityService.createMembership("中C", "部门经理");
	 identityService.createMembership("大A", "部门经理");
	 identityService.createMembership("小B", "总经理");
	 
	
	 System.out.println(true);
	
	}
	/**
	 * 启动流程实例
	 * */
	@Test
	public void startProcessInase() {
		processEngine.getRuntimeService().startProcessInstanceByKey("userrolegroup");
	System.out.println(true);
	}
	/**查询当前个人组任务信息，只要是小组成员，都可以查询*/
	@Test
	public void findPersonalTask() {
	Task task=	  processEngine.getTaskService()
		               .createTaskQuery()
		               .taskCandidateUser("中C")
		               .singleResult();
		      System.out.println(task.getName());
		      System.out.println(task.getId());
	}
	
}
