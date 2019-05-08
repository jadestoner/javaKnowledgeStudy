/**
 * 
 */
package com.jadeStone.thirdLib.quartz.chapter1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleTask implements Job {


	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println(Thread.currentThread().getName());
		System.out.println("i executing");
	}
}
