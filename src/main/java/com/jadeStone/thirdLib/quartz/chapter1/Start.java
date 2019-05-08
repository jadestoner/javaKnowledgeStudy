/**
 * 
 */
package com.jadeStone.thirdLib.quartz.chapter1;

import java.io.FileInputStream;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author 
 * 简单的一个demo
 * 便于了解基本概念
 * 
 */
public class Start {

	public static void main(String[] args)throws Exception {
		// 定义配置文件地址
		Properties p = new Properties();
		p.load(new FileInputStream("src/main/java/com/jadeStone/thirdLib/quartz/chapter1/chapter1.properties"));
		// 初始化scheduler
		SchedulerFactory sf = new StdSchedulerFactory(p);
		Scheduler s = sf.getScheduler();
		s.start();
		// 定义任务
		JobDetail detail = JobBuilder
				.newJob(SimpleTask.class)
				.withIdentity("SimpleTask1", "SimpleTask")
				.build();
		// 定义触发器
		TriggerBuilder tb = TriggerBuilder.newTrigger()
			.withIdentity("SimpleTask1", "SimpleTask")
			.forJob(detail)
			.startNow()
			// Schedule 时刻表的概念，而Scheduler是调度器的概念
			.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? *"));
		
		s.scheduleJob(detail, tb.build());
		
	}
}
