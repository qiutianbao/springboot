package com.jxtb.batch.common.service;

import com.jxtb.batch.common.ftp.ScpBean;

/**
 * Created by jxtb on 2019/7/3.
 */
public interface CommonConfigService {
    String queryDir(String remoteDirEnum);

    ScpBean queryScpBean(String sftpConfEnum);
}
