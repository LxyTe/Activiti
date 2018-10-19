package com.dist.processinstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {


	
			/**
			 * 得到默认的流程对象 
			 */
		ProcessEngine 	 processEngine=ProcessEngines.getDefaultProcessEngine();
	
	
	
	
	        
	/**部署流程定义从zip加载*/
	@Test
	public void deploymentProcessDefinitionZip() {
	
		InputStream in=this.getClass().getClassLoader().getResourceAsStream("diagrams/tt.zip"); 
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment= processEngine.getRepositoryService()//与流程定义和部署相关的service对象
				                .createDeployment()
				                .name("流程定义")
				                .addZipInputStream(zipInputStream)
				                .deploy();//完成部署
		System.out.println(deployment.getId()); 
		System.out.println(deployment.getName());
		 
	}
	/**启动流程实例*/
	@Test
	public void startProcessInstance() {
		//流程定义的key
		String processDefinitionKey="tt";
	ProcessInstance pInstance=	processEngine.getRuntimeService()
		             .startProcessInstanceByKey(processDefinitionKey);
		
	System.out.println("流程定义id:"+pInstance.getProcessDefinitionId());

	}
	/**查询当前个人的任务*/
	@Test
	public void findMyPersonalTask() {
	List<Task> list =	processEngine.getTaskService()//与正在执行的任务管理相关的service
		             .createTaskQuery() //创建查询对象
		             .taskAssignee("李四")//指定确切的某个人
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
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask() {
		
		processEngine.getTaskService()
		             .complete("7502");
		System.out.println("完成任务:"+true);
	}
	/**查询流程状态(正在执行还是结束) 查询的的表是act_ru_task */
	
	 @Test
	 public void isProcessEnd() {
		ProcessInstance pp= processEngine.getRuntimeService()
		              .createProcessInstanceQuery()
		              .processDefinitionId("1616")
		              .singleResult();//返回一个结果
	    if(pp==null) {
	    	System.out.println("查询的流程状态是否结束:"+true);
	    }else {
	    	System.out.println("查询的流程状态是否结束:"+false);
		}
	 }
	 /**查询历史任务*/
	 @Test
	 public void findHistoryTask() {
	List<HistoricTaskInstance>	historicTaskInstances  = processEngine.getHistoryService()
		              .createHistoricTaskInstanceQuery() //创建历史任务实例查询
		              .taskAssignee("张三") //根据办理人来查询
		              .list();
	if(historicTaskInstances !=null && historicTaskInstances.size()>0) {
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			System.out.println(historicTaskInstance.getId());
			System.out.println(historicTaskInstance.getAssignee());
		}
	} 
	 }

	 /**查询历史流程实例*/
		@Test
		public void findHistoryInstance() {
		HistoricProcessInstance hi=	processEngine.getHistoryService()
			             .createHistoricProcessInstanceQuery()
			             .processInstanceId("2501")
			             .singleResult();

		
			System.out.println(hi.getId());
			System.out.println(hi.getStartTime());
			System.out.println(hi.getEndTime());
		
		}
	
}
