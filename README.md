 #  Activiti学习笔记
 
 ### 工作流介绍
     Activiti是一个业务流程管理框架即为（BPM），它是覆盖了业务流程管理丶工作流丶服务协作等领域的一个
     流程框架。Activiti高效集成于eclispe等ide工具，我们可以直接手画出相对于业务的流程图，大大降低了xml的编码时间。

 ### 核心表介绍
      接触过工作流的都知道，它其实是将我们的业务和activiti自带的20几张表结合，然后实现我们需要的任务流程，接下
      来我给说一下，Activiti的表
      Activiti的相关表都是以act开头的，然后在配上不同的中间前缀 表示不同的作用
 
 > 资源库流程规则表
      
    act_re_deployment 	部署信息表, 我们将流程图发布成流程定义后，都可以在这张表中看到我们发布的信息
   
    act_re_model       流程设计模型部署表 （目前还没接触到，后续补充）
    
    act_re_procdef     流程定义数据表 ， 记录了 我们画流程图的时候定义的Key，和当前流程的版本号（在Activiti中，如果流程key相同，那么最新发布的流程定义，会自动覆盖以前发布的流程定义），还记录了，流程图的名字，部署信息表id等。
    
 >  运行时数据库表（重要）
 
     act_ru_execution      运行流程执行的实例表  ， 此表中记录了流程实例id，流程定义id，此表中的数据只有在流程启动后，才会存在，流程完毕后，此表清空。这样保证了，此表的数据量少，执行速度快。在单流程中，只存在一个流程执行对象，但是多任务流程中，可以存在多执行对象。
    
     act_ru_identitylink       (type :  participant  流程实例
      （参与者，相当于基本任务）, candidate 记录的是任务id  候选者,相当于组任务)  任务办理人表，主要在有组任务的时候使用
     
     act_ru_task   （重要）        正在执行的任务表(只有节点是UserTask的时候，该表中才有数据)  此表中记录了，执行对象id，流程实例id（一个流程只存在一个实例，但是可以有多个执行对象，）流程定义id，流程名字，流程使用人 ，流程的创建时间等信息。是我们频繁操作的一张表。
     
     act_ru_variable          正在执行的流程变量表  流程变量下面我们详细说明（其实就是执行流程时的一些参数）
      
  >  历史数据库表(重要)
       
       act_hi_actinst         历史节点表 , 记录了我们执行流程时候的全部节点，每一个节点信息都会被记录 包括（流程定义id，流程实例id，执行对象id，任务id，任务名称，任务的执行人，任务的开始和结束时间）
     
       act_hi_attachment     历史附件表，目前没用到（后续补充）
       
       
       act_hi_comment        历史意见表 后续补充
       
       act_hi_identitylink   历史流程人员表 ， 执行过流程的人员，都在此表中有记录（此表中有type字段，可对应上面的运行时流程人员表act_ru_identitylink ）
       
       act_hi_detail         历史详情表，提供历史变量的查询 后续补充
       
       act_hi_procinst		  历史流程实例表，此表主要记录了，流程的开始时间，结束时间，开始人，结束人。
      
       act_hi_taskinst		历史任务实例表 记录了 流程任务名称，执行人，任务开始时间和结束时间
       
       act_hi_varinst			历史变量表 在所有流程中使用的变量此表中都会记录此表还记录 流程实例id，执行对象id
       
   > 组织机构表
     
     1)act_id_group		       用户组信息表  一个用户组中有多少个角色
   
     2)act_id_info			       用户扩展信息表   目前没用到
     
     3)act_id_membership	   用户与用户组对应信息表   一个角色中有多少人
     
     4)act_id_user			       用户信息表       一共有多少个执行人
      此组织机构一般很难满足我们的业务，所以有时候需要自己创建扩展组织机构
      
   > 通用数据表
   
       act_ge_bytearray		二进制数据表 记录流程定义id，和流程图的具体信息
       
       act_ge_property			属性数据表存储整个流程引擎级别的数据,初始化表结构时，会默认插入三条记录 ,有一个字段表示下一个流程定义的id
       
   ## 核心配置文件activiti.cfg.xml
     
       上述的配置文件，在获取流程变量的时候会自动读取resources下的文件，名字必须是一样的
       Activiti核心配置文件，配置流程引擎创建工具的基本参数和数据库连接池参数。
      定义数据库配置参数：
     jdbcUrl: 数据库的JDBC URL。
     jdbcDriver: 对应不同数据库类型的驱动。
     jdbcUsername: 连接数据库的用户名。
     jdbcPassword: 连接数据库的密码。
     基于JDBC参数配置的数据库连接 会使用默认的MyBatis连接池。 下面的参数可以用来配置连接池（来自MyBatis参数）：
    jdbcMaxActiveConnections: 连接池中处于被使用状态的连接的最大值。默认为10。
    jdbcMaxIdleConnections: 连接池中处于空闲状态的连接的最大值。 
    jdbcMaxCheckoutTime: 连接被取出使用的最长时间，超过时间会被强制回收。 默认为20000（20秒）。
     jdbcMaxWaitTime: 这是一个底层配置，让连接池可以在长时间无法获得连接时， 打印一条日志，并重新尝试获取一个连接。（避免因为错误配置导致沉默的操作失败）。 默认为20000（20秒）。
     示例
     <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
	   	<!-- 连接数据的配置 -->
	   	<property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
	   	<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/activiti?useUnicode=true&amp;characterEncoding=utf8"></property>
	   	<property name="jdbcUsername" value="root"></property>
	   	<property name="jdbcPassword" value="123"></property>
	   	<!-- true没有表创建表 , false(默认) 检查数据库表的版本和依赖库的版本是否一致
	  	 create-drop 创建流程引擎时，创建表，流程结束时，删除表
	  	 -->
	  	<property name="databaseSchemaUpdate" value="true"></property>
	   </bean>
   
  ### 核心API
   > ProcessEngine 流程引擎
   1)在Activiti中最核心的类对象，其他的类对象都是由它而来。
    ProcessEngine processEngine=	ProcessEngines.getDefaultProcessEngine();
    上面的接口，会自动记载 activiti.cfg.xml 配置文件中内容
      
      作用：
     1. 可以产生RepositoryService  (流程定义服务对象) 
     RepositoryService  repositoryService= processEngine.getRepositoryService()
     
     2. 可以产生RuntimeService  （运行时服务对象）
          RuntimeService  runtimeService=    processEngine.getRuntimeService()
    
    3. 可以产生TaskService (任务服务对象)
    TaskService taskService =  processEngine.getTaskService
    
    4.HistoryService	历史管理(执行完的数据的管理)
     IdentityService	组织机构管理  等等
     
   >  RepositoryService 
    
      是Activiti的仓库服务类。所谓的仓库指流程定义文档的两个文件：bpmn文件和流程图片。(加载对应的流程图片生成流程图)
   1.1[流程定义相关代码](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/processdefinition/ProcessDefinitionTest.java) 
   
   >  RuntimeServic  
       
       是activiti的流程执行服务类。可以从这个服务类中获取很多关于流程执行相关的信息。
       目前我用的最多的就是用来启动流程服务
       	public void startProcessEngine() {
	ProcessInstance processInstance=	 processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
		             .startProcessInstanceByKey("tt"); //使用流程定义的key启动流程实施，key对应的值,使用key启动，默认启动的是最新版本
		System.out.println("流程实例id:"+processInstance.getId());
		System.out.println("流程定义id:"+processInstance.getProcessDefinitionId());
	}
   >   TaskService

     是activiti的任务服务类。可以从这个类中获取任务的信息，也可以创建组任务，个人任务，流程变量等。
        
 
  >  HistoryService
     
        是activiti的查询历史信息的类。在一个流程执行完成后，这个对象为我们提供查询历史信息。所有的历史信息都是经过HistoryService来进行查询的

 >  ProcessDefinition  
 
         流程定义类。可以从这里获得资源文件(bpmn图)等。
 
 >  ProcessInstance
  
         代表流程定义的执行实例。如某人想走一个流程，那么就会创建一个专门为此流程服务的流程实例。一个流程实例包括了所有的运行节点。我们可以利用这个对象来了解当前流程实例的进度等信息。流程实例就表示一个流程从开始到结束的最大的流程分支，即一个流程中流程实例只有一个。
   
 >  Execution
 
     Activiti用这个对象去描述流程执行的每一个节点。在没有并发的情况下，Execution就是同ProcessInstance。流程按照流程定义的规则执行一次的过程，就可以表示执行对象Execution。
	ProcessInstance的源代码：public interface ProcessInstance extends Execution {}
	
   ![流程实例](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E6%B5%81%E7%A8%8B%E5%AE%9E%E4%BE%8B.png)
      在单线流程中，如上图的贷款流程，ProcessInstance与Execution是一致的。
   ![多线流程](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E6%89%A7%E8%A1%8C%E5%AF%B9%E8%B1%A1.png) 
   多线流程：wire money(汇钱)和archive(存档)是并发执行的。	这个时候，总线路代表ProcessInstance，而分线路中每个活动代表Execution。
   
   总结 
   * 一个流程中，执行对象可以存在多个，但是流程实例只能有一个。
   * 当流程按照规则只执行一次的时候，那么流程实例就是执行对象。
	
   ## 案例
   ![HelloWorld图](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/diagrams/tt.png)
      >> 1.2[详细代码](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/tt/HelloTt.java)
      
   ## 管理流程定义 
   此处还是上面那个流程图  
  >> 1.3[管理流程定义](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/processdefinition/ProcessDefinitionTest.java) 
  
     上面代码主要写了
          1.部署流程定义（classpath路径加载文件）
	  1.2部署流程定义（zip格式文件加载）
          2.如何查询流程定义(多种查询方式)
	  3.删除流程定义
	  4.获取流程定义文档的资源（查看流程图附件）
	  5.查询最新版本的流程定义
	  6.删除流程定义（删除key相同的所有不同版本的流程定义
	  
  bpmn文件 
      
       流程规则文件。在部署后，每次系统启动时都会被解析，把内容封装成流程定义放入项目缓存中。Activiti框架结合这个xml文件自动管理流程，流程的执行就是按照bpmn文件定义的规则执行的，bpmn文件是给计算机执行用的。
  
  png图片
   
    在系统里需要展示流程的进展图片，图片是给用户看的。让用户知道流程是如何走的，有哪些人接收流程
    
   小结
     
      Deployment   部署对象
    1、一次部署的多个文件的信息。对于不需要的流程可以删除和修改。
    2、对应的表：
   	 act_re_deployment：部署对象表
  	 act_re_procdef：流程定义表
 	 act_ge_bytearray：资源文件表
 	 act_ge_property：主键生成策略表

	ProcessDefinition 流程定义
	1、解析.bpmn后得到的流程定义规则的信息，工作流系统就是按照流程定义的规则执行的。

    
   ### 流程变量
            
	    流程变量在整个工作流中扮演很重要的作用。例如：请假流程中有请假天数、请假原因等一些参数都为流程变量的范围。流程变量的作用域范围是只对应一个流程实例。也就是说各个流程实例的流程变量是不相互影响的。流程实例结束完成以后流程变量还保存在数据库中（存放到流程变量的历史表中  act_hi_varinst）。
	    
	  
   ![流程变量图](https://github.com/LxyTe/Activiti/blob/master/%E6%B5%81%E7%A8%8B%E5%8F%98%E9%87%8F.png) 
   1.4 [查看流程变量代码](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/processvariabletest/ProcessVariablesTest.java)
    
     说明：
      1)流程变量的作用域就是流程实例，所以只要设置就行了，不用管在哪个阶段设置
      2)基本类型设置流程变量，在taskService中使用任务ID，定义流程变量的名称，设置流程变量的值。
      3)Javabean类型设置流程变量，需要这个javabean实现了Serializable接口
      4)设置流程变量的时候，向act_ru_variable这个表添加数据
      5）流程变量的获取针对流程实例（即1个流程），每个流程实例获取的流程变量时不同的
      6）使用基本类型获取流程变量，在taskService中使用任务ID，流程变量的名称，获取流程变量的值。
      7）Javabean类型设置获取流程变量，除了需要这个javabean实现了Serializable接口外，还要求流程变量对象的属性不能发生变化，否则抛出异常。解决方案，固定序列化ID
      
  ![流程变量获取和设置接口](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E6%B5%81%E7%A8%8B%E5%8F%98%E9%87%8F%E8%AE%BE%E7%BD%AE%E5%92%8C%E8%8E%B7%E5%8F%96.png)
      
      获取和设置:
      1）RuntimeService对象可以设置流程变量和获取流程变量
      2）TaskService对象可以设置流程变量和获取流程变量
      3）流程实例启动的时候可以设置流程变量
      4）任务办理完成的时候可以设置流程变量
      5）流程变量可以通过名称/值的形式设置单个流程变量
      6）流程变量可以通过Map集合，同时设置多个流程变量
         Map集合的key表示流程变量的名称
         Map集合的value表示流程变量的值
      7) 流程变量支持的类型有 : String ,Integer ,short , long , double , booelean ,  date , serializable
    
    
    >总结
     在流程执行或者任务执行的过程中，用于设置和获取变量，使用流程变量在流程传递的过程中传递业务参数。
       对应的表：
               act_ru_variable：正在执行的流程变量表
               act_hi_varinst：流程变量历史表
       特别说明: 
            •setVariable和setVariableLocal的区别
	    setVariable：设置流程变量的时候，流程变量名称相同的时候，后一次的值替换前一次的值，而且可以看到TASK_ID的字段不会存放任务ID的值
	    setVariableLocal:设置流程变量的时候，针对当前活动的节点设置流程变量，如果一个流程中存在2个活动节点，对每个活动节点都设置流程变量，即使流程变量的名称相同，后一次的版本的值也不会替换前一次版本的值，但是 它里面的每个流程变量只属于单独的流程节点，当任务到下一个流程节点的时候，就无法获取上个节点中设置的流程变量
	     
### 流程历史执行记录
   1.5[流程历史执行记录查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/historyquery/HistoryQueryTest.java)
   
### 连线   
![连线](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E8%BF%9E%E7%BA%BF.png)
  1.6[连线设置流程变量查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/sequenceFlow/SequenceFlowTest.java)
       说明：
     1）使用流程变量，设置连线需要的流程变量的名称message，并设置流程变量的值对应,流程会按照指定的连线完成任务,设置的流程变量名字要和你判断的那个名字一致。
     
 ### 排他网关(串行)
 ![排他网关](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E6%8E%92%E4%BB%96%E7%BD%91%E5%85%B3.png)
 1.7[排他网关](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/gateway/GateWayTest.java)
 
 设置
    说明：
	1)一个排他网关对应一个以上的顺序流
	2)由排他网关流出的顺序流都有个conditionExpression元素，在内部维护返回boolean类型的决策结果。
	3)决策网关只会返回一条结果。当流程执行到排他网关时，流程引擎会自动检索网关出口，从上到下检索如果发现第一条决策结果为true或者没有设置条件的(默认为成立)，则流出。
	4)如果没有任何一个出口符合条件，则抛出异常
	使用流程变量，设置连线的条件，并按照连线的条件执行工作流，如果没有条件符合的条件，则以默认的连线离开。例如：
	![tu](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E6%8E%92%E4%BB%96%E7%BD%91%E5%85%B3%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E.png)
     
  ### 并行网关
  
  ![并行网关](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/gateway/parallelGateWay.png)
  1.8[并行网关查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/gateway/parallelGateWay.java)
     
      说明：
	1）一个流程中流程实例只有1个，执行对象有多个
	2）并行网关的功能是基于进入和外出的顺序流的：
	分支(fork)： 并行后的所有外出顺序流，为每个顺序流都创建一个并发分支。
	汇聚(join)： 所有到达并行网关，在此等待的进入分支， 直到所有进入顺序流的分支都到达以后， 流程就会通过汇聚网关。
	3）并行网关的进入和外出都是使用相同节点标识
	4）如果同一个并行网关有多个进入和多个外出顺序流， 它就同时具有分支和汇聚功能。 这时，网关会先汇聚所有进入的顺序流，然后再切分成多个并行分	支。
	5）并行网关不会解析条件。 即使顺序流中定义了条件，也会被忽略。
        6) 并行网关不需要是“平衡的”（比如， 对应并行网关的进入和外出节点数目不一定相等）。如图中标示是合法的：
 ![bx](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E5%B9%B6%E8%A1%8C%E7%BD%91%E5%85%B3.png)	    
    
  上图中的两个不同的并行网关Task3走到最后一个并行网关口的时候，会在哪里等待Task1和Task2执行完成然后回合，只有全部的并行网关或者汇合完毕，流程才算走完。
  ### 接收活动（receiveTask，即等待活动，其它的任务都是userTask，这里的任务不一样）
  ![等待任务](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E7%AD%89%E5%BE%85%E4%BB%BB%E5%8A%A1.png)
  1.9[等待任务查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/receivetask/ReceiveTaskTest.java)
  
  在任务创建后，意味着流程会进入等待状态， 直到引擎接收了一个特定的消息（执行下一步）， 这会触发流程穿过接收任务继续执行。
     
     说明：
	1）当前任务（一般指机器自动完成，但需要耗费一定时间的工作）完成后，向后推移流程，可以调用runtimeService.signal(executionId)，传递接收执行对象的id。
  
  ### 个人任务
  2.0[个人任务查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/personaltask/PersonalTask.java)
  
     设置个人任务办理人的时候
     1.可以直接在bpmn图形化界面指定（不推荐）
     2.使用流程变量指定(操作方式和上面设置流程变量差不多)
     3.使用监听类指定（注意;不需要指定办理人）
     
   ![兔兔](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E6%8C%87%E5%AE%9A%E4%BB%BB%E5%8A%A1%E5%8A%9E%E7%90%86%E4%BA%BA.png) 
   上面配置之后再加载流程图BPMN文件的时候，会自动指定任务的办理人
    
    总结
	个人任务及三种分配方式：
    1：在taskProcess.bpmn中直接写 assignee=“张三丰"
    2：在taskProcess.bpmn中写 assignee=“#{userID}”，变量的值要是String的。
         使用流程变量指定办理人
    3，使用TaskListener接口，要使类实现该接口，在类中定义：
         delegateTask.setAssignee(assignee);// 指定个人任务的办理人
    
     使用任务ID和办理人重新指定办理人：
      processEngine.getTaskService()//
                           .setAssignee(taskId, userId);
			   
   ### 组任务
   ![zu任务](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/resources/img/%E7%BB%84%E4%BB%BB%E5%8A%A1.png)
   组任务下面的那个框表示角色组 
   2.1[个人组任务查看](https://github.com/LxyTe/Activiti/blob/master/Activiti-parent/Activiti-One/src/main/java/com/dist/grouptask/GroupTask.java)
   其它操作和个人任务类似
     
        3）act_ru_identitylink表存放任务的办理人，包括个人任务和组任务，表示正在执行的任务
	4）act_hi_identitylink表存放任务的办理人，包括个人任务和组任务，表示历史任务
    区别在于：如果是个人任务 TYPE的类型表示participant（参与者）
			 如果是组任务TYPE的类型表示candidate（候选者）和participant（参与者）
     小结:
     组任务及三种分配方式：
    1：在taskProcess.bpmn中直接写 candidate-users=“小A,小B,小C,小D"
    2：在taskProcess.bpmn中写 candidate-users =“#{userIDs}”，变量的值要是String的。
         使用流程变量指定办理人
              Map<String, Object> variables = new HashMap<String, Object>();
              variables.put("userIDs", "大大,小小,中中");
    3，使用TaskListener接口，使用类实现该接口，在类中定义：
            //添加组任务的用户
     delegateTask.addCandidateUser(userId1);
     delegateTask.addCandidateUser(userId2);

   
  
  
