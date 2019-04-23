package com.jxtb.batch.listener;

import org.springframework.batch.core.JobExecutionListener;

import javax.batch.runtime.JobExecution;

/**
 * Created by jxtb on 2019/4/22.
 */
public class CsvJobListener implements JobExecutionListener {

    long startTime;
    long endTime;

    @Override
    public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println("任务处理开始...");
    }

    @Override
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        System.out.println("任务处理结束...");
        System.out.println("耗时:" + (endTime - startTime) + " ms");
    }
}
