package com.dist.service.impl;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dist.service.ChangeWorkFlowService;

@Component("changworkImpl")
public class ChangeWorkFlowServiceImpl implements ChangeWorkFlowService {

	 public  RepositoryService repositoryService;// 流程定义
	
	public  RuntimeService runtimeService;// 运行时
	
	public  TaskService taskService;// 任务
	
	public  FormService formService;// 任务表单
	
	public  HistoryService historyService;// 历史


	public void setRepositoryService(RepositoryService repositoryService) {
		// TODO Auto-generated method stub
		 this.repositoryService = repositoryService;
	}


	public void setRuntimeService(RuntimeService runtimeService) {
		// TODO Auto-generated method stub
		this.runtimeService = runtimeService;
	}

	
	public void setTaskService(TaskService taskService) {
		// TODO Auto-generated method stub
		this.taskService = taskService;
	}

	
	public void setFormService(FormService formService) {
		// TODO Auto-generated method stub
		this.formService = formService;
	}


	public void setHistoryService(HistoryService historyService) {
		// TODO Auto-generated method stub
		this.historyService = historyService;
	}

}
