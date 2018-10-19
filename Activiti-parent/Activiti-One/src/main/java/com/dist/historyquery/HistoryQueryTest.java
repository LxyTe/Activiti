package com.dist.historyquery;


import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQueryTest {


		ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
	
		/**查询历史流程实例*/
		@Test
		public void findHistoryProcessIntance() {
			List<HistoricProcessInstance>tt= processEngine.getHistoryService()
			              .createHistoricProcessInstanceQuery()
			              .processInstanceId("35001")
			              .list();
			if(tt!=null && tt.size()>0) {
				for (HistoricProcessInstance historicProcessInstance : tt) {
					System.out.println(historicProcessInstance.getId());
				    System.out.println(historicProcessInstance.getStartTime());
				    System.out.println(historicProcessInstance.getEndTime());
				}
			}
			              
		}
		
		/**查询历史任务信息*/		
		@Test
		public void findHistoryTaskInfo() {
		 List<HistoricTaskInstance> tt=	processEngine.getHistoryService()
			             .createHistoricTaskInstanceQuery()
			             .processInstanceId("2501")
			             .list();
			if(tt!=null && tt.size()>0) {
			for (HistoricTaskInstance historicTaskInstance : tt) {
			System.out.println(historicTaskInstance.getAssignee());
				System.out.println(historicTaskInstance.getName());
				System.out.println(historicTaskInstance.getEndTime());
			}
			}
		}
		
		/**查询所有已完成的活动信息*/
		@Test
		public void findHistoryActiviti() {
		List<HistoricActivityInstance> tt =processEngine.getHistoryService()
			             .createHistoricActivityInstanceQuery()
			             .processInstanceId("35001")
			             .list();
		for (HistoricActivityInstance historicActivityInstance : tt) {
		System.out.println(historicActivityInstance.getAssignee());	
	    System.out.println(historicActivityInstance.getActivityName());
	
		}
		}
	
		/**查询历史流程变量*/	
		 @Test
		 public void findHistoryVariable() {
		  HistoricVariableInstance historicVariableInstance  =	 processEngine.getHistoryService()
			              .createHistoricVariableInstanceQuery()
			              .variableName("请假变量信息")
			              .singleResult();
		  System.out.println(historicVariableInstance.getVariableName());
		  System.out.println(historicVariableInstance.getValue());
		  System.out.println(true);
		 }
}
