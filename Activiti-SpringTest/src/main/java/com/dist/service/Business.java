package com.dist.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.bpmn.model.Activity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;


/**
 * 业务接口
 * @author Administrator
 *
 */
public interface Business {

	//部署对象
	public void deploy(String deployBpmn , String deployPng , String deployName);
    
	//通过zip文件下部署对象
	public void deploy(File file , String deployName);

	 //删除部署对象
	public void deleteDeployGeneral(String deployid);

     //级联删除，删除所有流程定义信息（包括历史），可删除正在执行的流程
	public void deleteDeployCascade(String deployid);

    //查询部署对象列表根据名字
	public List<Deployment>findDeploymentList(String deployName);

    //根据部署名字，只返回一个结果
	public Deployment findDeployment(String deployName);

    //查询流程定义(获取最新版本的)
	public ProcessDefinition findProcessDefinition(String deployId);

    //查询流程定义的key
	public String findProcessKey(String deployName);

    //查询任务节点,返回当前节点对象
	public ActivityImpl findTaskNode(String  processIntanceId);

    //根据角色id，查询角色组信息
	 public List<org.activiti.engine.task.Task> findTaskListByRole(String roleId);
    //返回图片信息
	 public InputStream findImageInputStream(String deploymentId ,String imageName);

     //根据流程key启动任务
	   public ProcessInstance startProcess(String key);
	   
	   //根据流程实例id和任务办理人完成任务
	   public boolean finishTask(String processInstanceId, String lineName,String userId);
}
