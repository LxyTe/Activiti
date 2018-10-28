package com.dist.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dist.service.impl.ChangeWorkFlowServiceImpl;

@Component("Utils")
public class Utils {

	@Resource(name="changworkImpl")
	private ChangeWorkFlowServiceImpl changeWorkFlowServiceImpl;
	
	
   
	/**
     * 根据流程实例 ID，获取流程定义ID
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    public String getDefinitionId(String processInstanceId) {
        String definitionId = "";
        ProcessInstance pi = changeWorkFlowServiceImpl.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        definitionId = pi.getProcessDefinitionId().trim();

        return definitionId;
    }
    
    /**
     * 根据流程实例ID，获取流程定义ID(历史表)
     * 
     * @param processInstanceId
     * @return
     */
    public String getDefinitionIdByHistory(String processInstanceId) {
        String definitionId = "";
        HistoricProcessInstance pi = changeWorkFlowServiceImpl.historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        definitionId = pi.getProcessDefinitionId().trim();
        return definitionId;
    }
    /**
     * 根据流程定义ID，获取流程定义对象实体
     * 
     * @param definitionId
     *            流程定义ID
     * @return
     */
    public ProcessDefinitionEntity getProcessDefinitionEntity(
            String definitionId) {
        ProcessDefinitionEntity processDefinitionEntity =(ProcessDefinitionEntity)  changeWorkFlowServiceImpl.repositoryService
              .getProcessDefinition(definitionId);

        return processDefinitionEntity;
    }
    /**
     * 根据流程部署ID，获取流程定义对象实体
     * 
     * @param deploymentId
     *            流程部署ID
     * @return
     */
    public ProcessDefinition getProcessDefinition(String deploymentId) {
        ProcessDefinition processDefinition = changeWorkFlowServiceImpl.repositoryService
                .createProcessDefinitionQuery().deploymentId(deploymentId)
                .singleResult();

        return processDefinition;
    }
    
    /**
     * 根据流程实例ID，获取任务
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    public Task getTask(String processInstanceId) {
        Task task = changeWorkFlowServiceImpl.taskService.createTaskQuery()
                .processInstanceId(processInstanceId).singleResult();

        return task;
    }
   
    /**
     * 根据流程实例ID，获取运行时对象实体
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    public ExecutionEntity getExecutionEntity(String processInstanceId) {
        ExecutionEntity execution = (ExecutionEntity) changeWorkFlowServiceImpl.runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        return execution;
    }
    
    /**
     * 使用流程实例ID，查询正在执行的对象表，获取当前活动对应的流程实例对象
     * 
     * @param processInstanceId
     *            流程实例ID
     * @return
     */
    public ProcessInstance getProcessInstance(String processInstanceId) {
        ProcessInstance processInstance = changeWorkFlowServiceImpl.runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        return processInstance;
    }
    
    /**
     * 使用流程实例ID，查询历史流程实例
     * 
     * @param processInstanceId
     * @return
     */
    public HistoricProcessInstance getHistoricProcessInstance(
            String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = changeWorkFlowServiceImpl.historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return historicProcessInstance;
    }

    /**
     * 通过流程定义的key，级联删除流程
     * 
     * @param processKey
     *            流程定义key
     */
    public void deleteDeploy(String processKey) {
        List<ProcessDefinition> pdList = changeWorkFlowServiceImpl.repositoryService// 获取Service
                .createProcessDefinitionQuery()// 创建流程定义查询
                .processDefinitionKey(processKey)// 通过key查询
                .list();// 返回一个集合
        for (ProcessDefinition pd : pdList) {
            changeWorkFlowServiceImpl.repositoryService// 获取Service
                    .deleteDeployment(pd.getDeploymentId(), true);
        }

    }
    
    /**
     * 根据流程实例id，得到当前任务
     * @param processInstanceId 流程实例id
     * @return
     */
    public Task findTask(String processInstanceId) {
        return changeWorkFlowServiceImpl.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }
    

    /**
     * 根据流程实例id，得到上一个已完成节点
     * @param processInstanceId
     * @return
     */
    public HistoricActivityInstance findLastTask(String processInstanceId) {
        //得到以完成的上一任务节点
        List<HistoricActivityInstance>  haList=changeWorkFlowServiceImpl.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).finished().orderByHistoricActivityInstanceEndTime().desc().list();
        //已完成的上一任务
        HistoricActivityInstance ha = haList.get(0);
        return ha;
    }
    
    /**
     * 通过流程实例id得到执行对象
     */
    public Execution getExecution(String processInstanceId) {
        return changeWorkFlowServiceImpl.runtimeService.createExecutionQuery()
                  .processInstanceId(processInstanceId)
                  .singleResult();
    }
    
    /**
     * 根据流程实例id查询最后一个历史任务
     */
    public HistoricTaskInstance findLastHistTask(String processInstanceId) {
        List<HistoricTaskInstance> list = changeWorkFlowServiceImpl.historyService.createHistoricTaskInstanceQuery()
                      .processInstanceId(processInstanceId)
                      .orderByHistoricTaskInstanceEndTime()
                      .desc()
                      .list();
        return list.get(0);
    }
    
    /**
     * 通过流程定义key，得到所有节点数量和节点字符串
     * @param processKey 流程定义key
     * @return
     * @throws Exception
     */
    public Object[] getNodeId(String processKey){
        // 得到流程部署实例(得到部署最新的那一个流程)
        Deployment deployment =changeWorkFlowServiceImpl.repositoryService.createDeploymentQuery().processDefinitionKey(processKey).orderByDeploymenTime().desc().list().get(0);
        // 得到流程定义实例
        ProcessDefinition processDefinition = changeWorkFlowServiceImpl.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //得到流程定义实体
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) changeWorkFlowServiceImpl.repositoryService
                .getProcessDefinition(processDefinition.getId());
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
        // 初始化节点集合
        List<String> nodeList = new ArrayList<String>();
        for (ActivityImpl activity : activitiList) {
            nodeList.add(activity.getId());
        }
        StringBuffer nodeId = new StringBuffer();
        for (String node : nodeList) {
            nodeId.append("'").append(node).append("',");
        }
        Object[] obj = new Object[]{nodeList.size(),nodeId.substring(0, nodeId.length() - 1)};
        return obj;
    }
    /**
     * 通过业务列表，当前登录id，和名字查询，当前登录人的所有流程状态(是否执行完)
     * */
 
//    public List<Object> getApplyList(List<Object> list, String currentUserId, String currentUserName) {
//        if (list != null && list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                // 获取流程实例Id
//                String processInstanceId = list.get(i).get("PI_ID");                    
//                    // 设置办理人为当前登录用户
//                    list.get(i).set("CURRENTUSER", currentUserName);
//                    // 判断流程是否结束，如果结束则标志为完成
//                    // 通过流程实例id查找正在执行的对象
//                    Execution execution = changeWorkFlowServiceImpl.runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
//                            .singleResult();
//                    String status = "";
//                    if (execution != null) {
//                        status = "在办";
//                    } else {
//                        status = "完成";
//                    }
//                    list.get(i).set("AUDIT_STATUS", status);
//                }
//        }
//        return list;
//    }
    
    /**
     * 得到当前活动id
     * 
     */
 
    public String getActId(String processInstanceId) {
        String actId = "";
        // 通过流程实例id获取流程实例对象
        ProcessInstance processInstance = changeWorkFlowServiceImpl.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            actId = processInstance.getActivityId();
        }
        return actId;
    }
    
    /**
     * 查询当前环节的上一环节的详细信息
     * 
     */
//    public List<TP_BUSINESS> getWaitList(List<TP_BUSINESS> list, String currentUserId, String currentUserName) {
//        if(list != null && list.size() > 0) {
//            for(int i=0; i<list.size(); i++) {
//                //流程实例id
//                String pi_id = list.get(i).get("pi_id");    
//                    //设置当前办理人名称
//                    list.get(i).set("CURRENTUSER", currentUserName);
//                    //查询当前流程的历史任务
//                    List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
//                                  .processInstanceId(pi_id)
//                                  .orderByHistoricTaskInstanceEndTime()
//                                  .desc()
//                                  .list();
//                    if(hisList != null && hisList.size() > 0) {
//                        //设置上一个环节名称
//                        list.get(i).set("LAST_LINK", hisList.get(0).getName());
//                        //设置上个环节提交时间
//                        list.get(i).set("LAST_LINK_DATE", hisList.get(0).getEndTime());
//                        //得到上个办理人id
//                        String lastAsseId = hisList.get(0).getAssignee();
//                        List<Record> userList = Db.find("SELECT REAL_NAME FROM SYS_USER WHERE ID='"+lastAsseId+"'");
//                        if(userList != null && userList.size() > 0) {
//                            //设置上个办理人名称
//                            list.get(i).set("LAST_LINK_APPLICANT", userList.get(0).get("real_name"));
//                        }
//                        
//                }
//                if(list.get(i).get("AUDIT_STATUS").equals("1")) {
//                    list.get(i).set("AUDIT_STATUS", "在办");
//                }else {
//                    list.get(i).set("AUDIT_STATUS", "完结");
//                }
//            }
//        }
//        return list;
//    }
    

    /**
     * 得到当前执行对象
     */
    public Execution getCurrentExecution(String processInstanceId) {
        Execution execution = changeWorkFlowServiceImpl.runtimeService.createExecutionQuery()
                                            .processInstanceId(processInstanceId)
                                            .singleResult();
        return execution;
    }
    
    /**
     * 指定当前用户对象完成某个任务
     */
    public boolean completeProcess(String processInstanceId,String status,String currentUserId) {
        boolean result = false;
        String elString="${message=='完成'}";
        Map<String,Object> variables = new HashMap<String, Object>();
        Task task = changeWorkFlowServiceImpl.taskService.createTaskQuery()
                               .processInstanceId(processInstanceId)
                               .singleResult();
        if(task != null) {
            //获取任务id
            String taskId = task.getId();
            //拾取任务
            if(task.getAssignee() == null) {
                //采用当前要用户拾取任务
                changeWorkFlowServiceImpl.taskService.claim(taskId, currentUserId);
            }    
            //得到下一个节点id            
            String nextActId = this.viewNextAct(processInstanceId, elString);
            //如果下一个节点id是endevent1（结束节点），则根据status判断是同意还是拒绝用于最后是否保存数据
            if(nextActId.toString().trim().equals("endevent1")) {
                variables.put("message", "完成");
            }else {
                variables.put("message", status);
            }            
            changeWorkFlowServiceImpl.taskService.complete(taskId,variables);
            result = true;
        }
        return result;
    }
    
    /**
     * 得到下一任务节点id
     * */
    public String viewNextAct(String processInstanceId, String elString) {
        String nextActId = "";
        //获取流程实例对象
        ProcessInstance processInstance = changeWorkFlowServiceImpl.runtimeService.createProcessInstanceQuery()
                      .processInstanceId(processInstanceId)
                      .singleResult();
        
        if(processInstance != null) {
            //获取当前节点id
            String activityId = processInstance.getActivityId();
            //获取流程定义id
            String processDefinitionId = processInstance.getProcessDefinitionId();
            //获取流程定义实体对象
            ProcessDefinitionEntity processDefinitionEntity= (ProcessDefinitionEntity)changeWorkFlowServiceImpl.repositoryService.getProcessDefinition(processDefinitionId);
            if(processDefinitionEntity != null) {
                //获取当前节点对象
                ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
                if(activityImpl != null) {
                    //得到当前节点所有出线信息
                    List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
                    if(pvmList != null && pvmList.size() > 1) {
                        for(PvmTransition pvm:pvmList) {
                            //得到连线的表达式
                            String conditionText = (String)pvm.getProperty("conditionText");
                            //看得到的连线名称和传递过来的名称是否匹配
                            if(elString.equals(conditionText)) {
                                ActivityImpl a = (ActivityImpl)pvm.getDestination();
                                //获取下一个节点的id
                                nextActId = a.getId();                                
                            }
                        }
                    }
                }
            }
        }
        return nextActId;              
    }
    
    /**
     * 查看当前节点是否执行完成
     */
    public boolean revoke(String processInstanceId) {
        // 根据流程实例id获取任务对象
        Task task = changeWorkFlowServiceImpl.taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String taskId = task.getId();
        // 结束流程时设置流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("message", "撤销");
        changeWorkFlowServiceImpl
        .taskService.complete(taskId, variables);
        // 查看流程是否结束(execution为空则结束)
        Execution execution = changeWorkFlowServiceImpl.runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        if (execution == null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 查看流程图
     */
    /**
     * 查看流程图
     * @param deploymentId 流程部署id
     * @param diagramResourceName 资源图片名称
     * @param response 将数据向页面输出
     * @throws IOException 
     */
//    public void showView(String deploymentId,String diagramResourceName,HttpServletResponse response) throws Exception{
//        //调用接口得到流程图
//        InputStream inputStream = changeWorkFlowServiceImpl.repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
//        OutputStream out = response.getOutputStream();
//        for(int b = -1;(b=inputStream.read())!=-1;){
//            out.write(b);
//        }
//        out.close();
//        inputStream.close();
//    }
    
    /**
     * 传入流程实例id，查看流程图片
     * 
     */
    public InputStream viewImg(String processInstanceId) {
        InputStream in = null;
        // 获取流程实例对象
        HistoricProcessInstance  processinstance = changeWorkFlowServiceImpl.historyService.createHistoricProcessInstanceQuery()
                      .processInstanceId(processInstanceId)
                      .singleResult();
        if (processinstance != null) {
            // 获取流程定义id
            String processDefinitionId = processinstance.getProcessDefinitionId();
            // 获取流程定义对象
            ProcessDefinition processDefinition = changeWorkFlowServiceImpl.repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId).singleResult();
            // 部署id
            String deployId = processDefinition.getDeploymentId();
            // 文件名称
            String imgName = processDefinition.getDiagramResourceName();
            // 获取输入流
            in = changeWorkFlowServiceImpl.repositoryService.getResourceAsStream(deployId, imgName);
        }
        return in;
    }
    
    /**
     * 查看流程图的当前活动节点
     * @param taskId 任务id
     * @return [0]x、[1]y、[2]width、[3]height、[4]流程部署id、[5]资源图片路径
     * @throws Exception
     */
    public Object[] showCurrentView(String processInstanceId) throws Exception{
        //无论流程是否走完，得到的都是最后一条记录(ACT_HI_TASKINST)
        HistoricTaskInstance h = changeWorkFlowServiceImpl.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list().get(0);
        String processDefinitionId = h.getProcessDefinitionId();//得到流程定义id
        //流程定义实体
        ProcessDefinitionEntity pdm = (ProcessDefinitionEntity) changeWorkFlowServiceImpl.repositoryService.getProcessDefinition(processDefinitionId);
        ProcessInstance pi = changeWorkFlowServiceImpl.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        
        /*因为activiti框架的缓存会话机制，所以我在这个位置将“流程部署id和支援图片路径取出来”*/
        ProcessDefinition pd = changeWorkFlowServiceImpl.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId.trim()).singleResult();
        String deploymentId = pd.getDeploymentId(); //流程部署id
        String diagramResourceName = pd.getDiagramResourceName(); //流程资源图片路径
        /*因为activiti框架的缓存会话机制，所以我在这个位置将“流程部署id和支援图片路径取出来”*/
        
        Object[] view = new Object[6];
        view[4] = deploymentId;
        view[5] = diagramResourceName;
        //因为有可能流程已经走完，所有还是判断哈，免得空指针异常
        if(pi != null){
            ActivityImpl ai = pdm.findActivity(pi.getActivityId());
            //根据活动ID  获取活动实例
            ActivityImpl activityImpl = pdm.findActivity(pi.getActivityId());
            view[0] = activityImpl.getX();
            view[1] = activityImpl.getY();
            view[2] = activityImpl.getWidth(); //宽度
            view[3] = activityImpl.getHeight(); //高度
        }
        
        return view;
    }
}
