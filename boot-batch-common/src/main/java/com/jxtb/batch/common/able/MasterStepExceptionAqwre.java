package com.jxtb.batch.common.able;

import org.springframework.batch.core.StepExecution;

/**
 * Created by jxtb on 2019/6/28.
 */
public interface MasterStepExceptionAqwre {
    void setMasterStepExceptionAware(StepExecution stepException);
}
