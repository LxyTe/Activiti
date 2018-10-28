import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	
	
	public static void main(String[] args) {
		 ApplicationContext applicationContext= new ClassPathXmlApplicationContext("classpath:spring.xml");
		 
		 System.out.println(applicationContext);
		 System.out.println(true);
	}
	
	@org.junit.Test
	public void deploy() {
	ProcessEngine processEngine=	ProcessEngines.getDefaultProcessEngine();
	  processEngine.getRepositoryService().createDeployment()
	  .name("报销流程").addClasspathResource("diagrams/Test.bpmn")
	  .addClasspathResource("diagrams/Test.png").deploy();
	
	}
}
