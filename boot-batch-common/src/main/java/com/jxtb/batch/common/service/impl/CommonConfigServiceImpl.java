package com.jxtb.batch.common.service.impl;

import com.jxtb.batch.common.ftp.ScpBean;
import com.jxtb.batch.common.service.CommonConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by jxtb on 2019/7/5.
 */
@Service(value = "commonConfigService")
public class CommonConfigServiceImpl implements CommonConfigService{

    private static final Logger logger = LoggerFactory.getLogger(CommonConfigServiceImpl.class);


    @Override
    public String queryDir(String remoteDirEnum) {
        return null;
    }

    @Override
    public ScpBean queryScpBean(String sftpConfEnum) {
        return null;
    }
}
