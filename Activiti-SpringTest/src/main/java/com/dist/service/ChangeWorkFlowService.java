package com.dist.service;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

public interface ChangeWorkFlowService  {

	
	public void setRepositoryService(RepositoryService repositoryService) ;

    public void setRuntimeService(RuntimeService runtimeService);

    public void setTaskService(TaskService taskService);
    
    public void setFormService(FormService formService);

    public void setHistoryService(HistoryService historyService);
}
