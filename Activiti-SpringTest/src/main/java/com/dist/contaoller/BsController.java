package com.dist.contaoller;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dist.util.Utils;

@Controller
public class BsController {
	
	  private final static Logger logger= LoggerFactory.getLogger(BsController.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositotyService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;
	
	@Resource(name="Utils")
	private Utils utils;

	@RequestMapping(value="/startProcess.te" )
	@ResponseBody  //根据流程key，启动流程
	public String startProcess(@RequestParam(value="key") String key) {
        
		 logger.info("key{}" + key);
		 ProcessInstance processInstance= runtimeService.startProcessInstanceByKey(key);
	       
		 logger.info("ProcessInstance{}" , processInstance.getId());
	  return "true";
	}
	
	//审批，然后设置对应的审批信息
	@RequestMapping(value="/Managerapproval.te" )
	@ResponseBody
	public String Managerapproval(@RequestParam(value="id") String ProcessInstanceid , 
			                      @RequestParam (value="mesasgeName") String messagaeName ,
			                      @RequestParam (value="mesasge") String messagae , 
			                      @RequestParam (value="assignee") String assignee) {
		
		//根据流程实例id，获取最新的任务对象
	   Task task=  utils.getTask(ProcessInstanceid);
		Map<String, Object> variable = new HashMap<String, Object>();
		variable.put(messagaeName, messagae);
		 task.setAssignee(assignee);//设置任务办理人
		 taskService.complete(task.getId(),variable);
		return "true";
	}
	
	//流程走完查看节点信息(历史节点)
	@RequestMapping(value="/Nodeinformation.te" )
	@ResponseBody
	public String Nodeinformation(@RequestParam(value="id") String ProcessInstanceid ) {
		
  List<HistoricTaskInstance>tt=	historyService.createHistoricTaskInstanceQuery()
		              .processInstanceId(ProcessInstanceid)
		               .orderByHistoricTaskInstanceStartTime()
		              .asc()
		              .list();
 System.out.println(tt.size());
     for (HistoricTaskInstance historicTaskInstance : tt) {
    	 System.out.println("数据是:"+historicTaskInstance.getName());
    	  logger.info( "节点名称 **********"+ historicTaskInstance.getName());
		  logger.info( "开始时间 **********"+ historicTaskInstance.getStartTime().toString());
	      logger.info(" 结束时间 **********"+historicTaskInstance.getEndTime()); 
     }
		return  "4";
	}
	
	@RequestMapping(value="/deleteProcess.te" )
	@ResponseBody  //根据流程实例id删除流程
	public String deleteProcess(@RequestParam(value="id") String processIntancesId,
			                    @RequestParam(value="deleteReason" )String deleteReason) {
        
		 logger.info("key{}" + processIntancesId);
		 runtimeService.deleteProcessInstance(processIntancesId, deleteReason);//第二个参数为删除原因,删除之后的信息会存在于act_hi_procinst 表
	     
	  return "true";
	}
	
	@RequestMapping(value="/deleteProcess.te" )
	@ResponseBody  //删除整个流程定义（不建议使用）
	public String deleteProcessDeploy(@RequestParam(value="key") String processDeployKey) {
        
		 logger.info("key{}" + processDeployKey);
		 //删除流程定义 ， 忽略是否有流程正在运行
		  repositotyService.deleteDeployment(processDeployKey, true);
	     
	  return "true";
	}
	
	@RequestMapping(value="/deleteProcess.te" )
	@ResponseBody  //查看流程图 
	public String findProcessView(@RequestParam(value = "processIntanceId") String processIntanceId , 
			            HttpServletResponse response ) throws IOException {
        
	InputStream inputStream=	utils.viewImg(processIntanceId);
	 OutputStream out = response.getOutputStream();
   for(int b = -1;(b=inputStream.read())!=-1;){
       out.write(b);
   }
   out.close();
   inputStream.close();
	     
	  return "true";
	}
	
	
	
	
	/***
	 * 节点状态，(待定)
	 * 
	 * 使用流程变量将所有人的节点信息，设置为false，已经走完的就这种为true
	 */
	
	
	
	/***
	 * 
	 * 得到节点一共有多少个，和节点人信息
	 */
	
	@RequestMapping(value="/nodeList.te" )
	@ResponseBody 
	public String nodeList(@RequestParam(value="id") String processIntanceId) {
        
		 logger.info("Id{}" + processIntanceId);
		 
		 ProcessInstance  processInstance=  runtimeService.createProcessInstanceQuery()
				 .processInstanceId(processIntanceId)
		         .singleResult();
	   Object [] tt=  utils.getNodeId(processInstance.getProcessDefinitionId());
	   System.out.println("一共有多少个人流转:*****"+tt.length);
	   return "true";
	}
	
	
}
