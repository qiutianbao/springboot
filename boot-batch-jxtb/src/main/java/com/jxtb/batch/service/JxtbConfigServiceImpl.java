package com.jxtb.batch.service;

import com.jxtb.batch.common.ftp.ScpBean;
import com.jxtb.batch.common.service.CommonConfigService;
import org.springframework.stereotype.Service;

/**
 * Created by jxtb on 2019/7/5.
 */
@Service(value = "jxtbConfigService")
public class JxtbConfigServiceImpl implements CommonConfigService{
    @Override
    public String queryDir(String remoteDirEnum) {
        System.out.println("queryDirqueryDir");
        return null;
    }

    @Override
    public ScpBean queryScpBean(String sftpConfEnum) {
        System.out.println("queryScpBeanqueryScpBean");
        return null;
    }
}
