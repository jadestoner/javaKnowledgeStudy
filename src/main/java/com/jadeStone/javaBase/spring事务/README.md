# spring事务

## 回顾事务
 针对mysql
- 事务基本特性：ACID
- 并发下几个问题：脏读/丢失修改/不可重复读/幻读
- 几种事务隔离级别
- mysql为什么用不可重复读作为默认的事务隔离级别?
- 互联网项目中为什么推荐使用【读已提交】这一级别?
- 断网断电后，数据库恢复时是怎么处理中断的事务的?

## spring中对事务的封装

参考[：](https://blog.csdn.net/qq_34337272/article/details/80394121)




### 注解标签
<tx:annotation-driven transaction-manager="transactionManager" />
#### 理解<br/>
-   支持事务的注解（@transactional）,如同
<mvc:annotation-driven> 就是支持mvc注解的，就是使Controller中可以使用MVC的各种注解。
-    spring 是使用 aop 通过 asm 操作Java字节码的方式来实现对方法的前后事务管理的。
-   只会查找和它在相同的应用上下文件中定义的bean上面的@Transactional注解(即，支持controller上使用，但是需要在dispatcher的应用上下文上也放上一份这样的配置，不推荐使用)
#### spring的解析过程
