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
       
     
       
       
