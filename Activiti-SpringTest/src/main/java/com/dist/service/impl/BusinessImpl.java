package com.dist.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.service.Business;
import com.dist.util.Utils;

@Service
public class BusinessImpl implements Business{


	@Resource(name="changworkImpl")
	private ChangeWorkFlowServiceImpl ChangeWorkFlowServiceImpl;
	
	@Autowired
	private Utils utils;

	public void deploy(String deployBpmn, String deployPng, String deployName) {
		// TODO Auto-generated method stub
	
		ChangeWorkFlowServiceImpl.repositoryService.createDeployment()
		.name(deployName).addClasspathResource(deployBpmn)
		.addClasspathResource(deployPng).deploy();
		
	}


	public void deploy(File file, String deployName) {
		// TODO Auto-generated method stub
		    try {
				ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
			   ChangeWorkFlowServiceImpl.repositoryService.createDeployment()
			     .addZipInputStream(zipInputStream).deploy();
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	public void deleteDeployGeneral(String deployid) {
		// TODO Auto-generated method stub
		 //普通删除，如果删除正在执行的流程，会抛异常
		try {
			ChangeWorkFlowServiceImpl.repositoryService.deleteDeployment(deployid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void deleteDeployCascade(String deployid) {
		// TODO Auto-generated method stub
		ChangeWorkFlowServiceImpl.repositoryService.deleteDeployment(deployid, true);
	}


	public List<Deployment> findDeploymentList(String deployName) {
		
		  List<Deployment>tt=null;
		  try {
			ChangeWorkFlowServiceImpl.repositoryService.createDeploymentQuery().orderByDeploymenTime().asc().list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tt;
	}


	public Deployment findDeployment(String deployName) {
		Deployment deployment=null;
		try {
			deployment =ChangeWorkFlowServiceImpl.repositoryService.createDeploymentQuery()
					.deploymentName(deployName)
					.orderByDeploymenTime()
					.desc()
					.singleResult();
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		return deployment;
	}


	public ProcessDefinition findProcessDefinition(String deployId) {
	ProcessDefinition processDefinition=null;
	try {
		processDefinition =ChangeWorkFlowServiceImpl.repositoryService
				.createProcessDefinitionQuery()
				.deploymentId(deployId)
				.orderByProcessDefinitionVersion().desc().singleResult();
	} catch (Exception e) {
		e.printStackTrace();
	}
		return processDefinition;
	}


	public String findProcessKey(String deployName) {
		 Deployment deployment=findDeployment(deployName);
		 ProcessDefinition pd= findProcessDefinition(deployment.getId());
		 
		return pd.getKey();
	}


	public ActivityImpl findTaskNode(String processIntanceId) {
	
		ExecutionEntity executionEntity=null; //执行对象
		ProcessDefinitionEntity processDefinitionEntity =null;//定义对象 
		ActivityImpl activity=null;
		
		try {
			String definitionId=utils.getDefinitionId(processIntanceId);
			processDefinitionEntity = utils.getProcessDefinitionEntity(definitionId);
		  executionEntity=  utils.getExecutionEntity(processIntanceId);
		
	      // 获取流程所有节点信息
          List<ActivityImpl> activitiList = processDefinitionEntity
                  .getActivities();
          // 遍历所有节点信息
          for (ActivityImpl activityImpl : activitiList) {
              // 找到当前节点信息
              if (executionEntity.getActivityId().equals(activityImpl.getId())) {
                  activity = activityImpl;
              }
          }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activity;
	}

    public List<Task> findTaskListByRole(String roleId) {
        List<Task> list = ChangeWorkFlowServiceImpl.taskService.createTaskQuery()
                .taskCandidateUser(roleId).orderByTaskCreateTime().asc().list();

        return list;
    }


	    public InputStream findImageInputStream(String deploymentId,
	            String imageName) {
	        return ChangeWorkFlowServiceImpl.repositoryService.getResourceAsStream(deploymentId, imageName);
	    }


	public ProcessInstance startProcess(String key) {
		   System.out.println("key...................................");
	        ProcessInstance processInstance = ChangeWorkFlowServiceImpl.runtimeService.startProcessInstanceByKey(key);
	        System.out.println(processInstance);
	        // 获取任务对象
	        return processInstance;
	}


	public boolean finishTask(String processInstanceId, String lineName, String userId) {
		 String taskId = "";
	        Map<String, Object> variables = new HashMap<String, Object>();

	        // 使用任务ID,查询任务对象
	        Task task = utils.getTask(processInstanceId);

	        taskId = task.getId().trim();

	        // 拾取办理人
	        ChangeWorkFlowServiceImpl.taskService.claim(taskId, userId);

	        // 设置连线名称
	        variables.put("msg", lineName);

	        // 完成任务
	        ChangeWorkFlowServiceImpl.taskService.complete(taskId, variables);

	        // 判断流程是否结束
	        ProcessInstance pi = utils.getProcessInstance(processInstanceId);

	        if (pi == null) {
	            return true;
	        }

	        return false;
	}

	
}
