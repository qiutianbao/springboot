package com.jxtb.batch.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 这个类用于手动触发job执行
 * Created by jxtb on 2019/7/4.
 */
@RestController
public class JobLauncherController {

    private static final Logger logger = LoggerFactory.getLogger(JobLauncherController.class);

    private static final String JOB_NAME = "jobName";
    private static final String OVER_RIDE = "override"; //传入参数为 1 可以重复执行

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRegistry jobRegistry;

    // http://localhost:8080/executeJob?jobName=testJob&override=1&date=20190708
    @RequestMapping(value = "/executeJob", method = RequestMethod.GET)
    public String launch(@RequestParam String jobName, HttpServletRequest request) throws Exception{
        JobParameters jobParameters = buildParameters(request, jobName);
        jobLauncher.run(jobRegistry.getJob(jobName),jobParameters);
        return "ok";
    }

    private JobParameters buildParameters(HttpServletRequest request, String jobName){
        JobParametersBuilder builder = new JobParametersBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        boolean overrideFlag = false;
        while (paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            if(!JOB_NAME.equals(paramName)){
                if(OVER_RIDE.equals(paramName) && request.getParameter(paramName).equals("1")){
                    overrideFlag = true;
                }
                builder.addString(paramName, request.getParameter(paramName));
            }
        }
        //批量日期
        String  date = request.getParameter("date");

        builder.addString("date", date);
        //override情况下需要续批，需指定rundate
        if(overrideFlag){
            String runDate = request.getParameter("runDate");
            builder.addLong("runDate", StringUtils.isBlank(runDate) ? System.currentTimeMillis() : Long.valueOf(runDate));
        }
        return builder.toJobParameters();
    }

}
