package com.jxtb.batch.common.enums;

/**
 * Created by jxtb on 2019/7/4.
 */
public enum  ConfigTypeEnum {
    //FTP配置
    SFTP_CONFIG_JXTB("0000","吉祥天宝FTP服务器配置信息"),
    SFTP_CONFIG_BD("0001","百度FTP服务器配置信息"),

    SFTP_PROXY_OUT_HTTP("HTTP","吉祥天宝代理服务器HTTP"),
    SFTP_PROXY_OUT_SCOK4("SCOK4","吉祥天宝代理服务器SCOK4"),
    SFTP_PROXY_OUT_SOCK5("SCOK5","吉祥天宝代理服务器SCOK4"),

    //目录配置
    SFTP_SFTP_LOCAL("2000","本地文件目录"),
    SFTP_SFTP_REMOTE("2001","吉祥天宝远程目录");

    private String code;
    private String value;
    ConfigTypeEnum(String code, String value){
        this.code = code;
        this.value = value;
    }

    public static ConfigTypeEnum byCode(final  String code){
        for(ConfigTypeEnum item : ConfigTypeEnum.values()){
            if(item.getCode().equals(code)){
                return item;
            }
        }
        return null;
    }

    public static String getValueByCode(final String code){
        ConfigTypeEnum item = byCode(code);
        if(null != item){
            return item.getValue();
        }
        return null;
    }

    public static boolean isCodeInEnum(String code){
        ConfigTypeEnum item = byCode(code);
        return (null != item) ? true : false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
