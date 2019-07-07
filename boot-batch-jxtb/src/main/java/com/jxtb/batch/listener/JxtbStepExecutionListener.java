package com.jxtb.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Created by jxtb on 2019/7/5.
 */
public class JxtbStepExecutionListener implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(JxtbStepExecutionListener.class);
    public JxtbStepExecutionListener(){

    }
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("JxtbStepExecutionListener.beforeStep()");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("JxtbStepExecutionListener.afterStep()");
        return stepExecution.getExitStatus();
    }
}
