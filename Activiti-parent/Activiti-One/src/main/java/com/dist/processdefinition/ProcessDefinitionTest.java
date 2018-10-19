package com.dist.processdefinition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.h2.store.fs.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {

	/**
	 * 得到默认的流程对象 
	 */
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
  
	/**部署流程定义从classpath加载*/
	@Test
	public void deploymentProcessDefinition() {
		Deployment deployment= processEngine.getRepositoryService()//与流程定义和部署相关的service对象
				                .createDeployment()
				                .name("流程定义")
				                .addClasspathResource("diagrams/tt.bpmn")
				                .addClasspathResource("diagrams/tt.png")
				                .deploy();//完成部署
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
		 
	}
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
	/**查询流程定义*/
	@Test
	public void findProcessDefinition() {
	 List<ProcessDefinition> processDefinition=	processEngine.getRepositoryService()
		             .createProcessDefinitionQuery()//创建流程定义查询
		             
		             //.deploymentId("20001")//使用部署id进行查询
		             //.processDefinitionId("tt:6:20004")//使用流程id查询
		             //.processDefinitionKey("tt") //使用key查询
		             //.processDefinitionName("Hellott")//根据名字来查询
		             //.listPage(firstResult, maxResults)分页查询
		               .orderByProcessDefinitionId().asc()//按照id排序
		             .list();//返回一个结果集，可根据查询条件来选择看是否返回结果集合或是单一结果
		             // .singleResult();  //表示返回唯一结果   
	        /**API接口太多这里就不一一测试了*/
		  System.out.println(processDefinition.size());        
		  
	}
		  /**删除流程定义
		   * 
		   * 注意：
		   *     第一种删除可以删除没有启动的流程定义，如果流程定义已经启动那么将无法进行删除抛异常
		   *     第二种可以删启动的流程定义，相当于一个级联删除，把涉及到想要删除的id都删除，boolean的默认值为false
		   * 
		   * */
		  @Test
		  public void deletePorcessDefinition() {
			  processEngine.getRepositoryService()
			               .deleteDeployment("1");
			//  processEngine.getRepositoryService().deleteDeployment(deploymentId, cascade);     
			  System.out.println(true);
		  }
	/**查看流程图片
	 * @throws IOException */
		  @Test
		  public void viewPhoto() throws IOException {
			List<String> listData= processEngine.getRepositoryService()
			              .getDeploymentResourceNames("1616");//查询的时候会返回两条记录，一个bpmn，一个png，
		  for (String dataName : listData) {
			//我们只需要取s得.png即可，这里做一下过滤
			  if(dataName.indexOf(".png") >0) {
				 InputStream is= processEngine.getRepositoryService()
				               .getResourceAsStream("1616", dataName);
				File file=  new File("D:/"+dataName);
				FileOutputStream fos = new FileOutputStream(file);

				byte[] b = new byte[1024]; 
				int len = 0;
				while((len = is.read(b)) != -1)
				{
				fos.write(b,0,len);
				}
				fos.flush();
				fos.close();
				System.err.println(true);
			  }
		}
		  
		  }
		 
		  /**获得最新版本的流程定义对象
		   * 原理：
		   *     1.先获取所有key相同的版本定义
		   *     2.将获取的信息放入map集合中
		   *     Map的key就是 流程定义对象的key，这样key就会被最新的版本覆盖，然后转成list，方便遍历
		   *     
		   * 
		   * */
		  @Test
		  public void findNewVersion() {
		List<ProcessDefinition> list=	  processEngine.getRepositoryService()
	                       .createProcessDefinitionQuery()
	                       .orderByProcessDefinitionVersion().asc()//正着排序
	                       .list();
		  //LinkedHashMap有自动排序的功能
		  Map<String, ProcessDefinition> map=new LinkedHashMap<String, ProcessDefinition>();
		  if(list!=null) {
			for (ProcessDefinition processDefinition : list) {
				map.put(processDefinition.getKey(), processDefinition);
			}  
		  }
		  /**Map集合转成List*/
		  List<ProcessDefinition> pdList=new ArrayList<ProcessDefinition>(map.values());
		   for (ProcessDefinition processDefinition : pdList) {
			System.out.println(processDefinition.getVersion());
		}
		  }
		/**删除流程定义*/
		  @Test
		  public void deleteProcessDefinitionByKey() {
		 List<ProcessDefinition>  list=	 processEngine.getRepositoryService()
			               .createProcessDefinitionQuery()			               
			               .processDefinitionKey("GateWay")
			               .list();


		   for(int i=0;i<list.size();i++) {//只留最新版本的流程定义，删除不想要的低版本信息
		    processEngine.getRepositoryService()
		                 .deleteDeployment(list.get(i).getDeploymentId(),true);
		                 
		   }
		   System.out.println(true);
		  }
}

