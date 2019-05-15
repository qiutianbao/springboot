package com.jxtb.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jxtb on 2019/4/27.
 */
public class BatchXmlApplication {
    public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("job.xml");

        SimpleJobLauncher launcher = (SimpleJobLauncher) ctx.getBean("laucher");
        Job job = (Job) ctx.getBean("jobExample");
        System.out.println(launcher);
        System.out.println(job);
        launcher.run(job, new JobParameters());
        ctx.close();

    }

}
