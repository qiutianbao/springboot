package com.jxtb.batch.common.ftp;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * Created by jxtb on 2019/7/3.
 */
@Data
public class ScpBean {
    private String remoteAddress;
    private int remotePort;
    private String username;
    private String password;
    private String prvKeyPath;

    public boolean validate(){
        if(this.remotePort == 0){
            this.remotePort = 22;
        }
        if((StringUtils.isBlank(this.remoteAddress)) || (StringUtils.isBlank(this.username)) || (StringUtils.isBlank(this.password)) || (this.remotePort <= 0)){
            return false;
        }
        return true;
    }

}

