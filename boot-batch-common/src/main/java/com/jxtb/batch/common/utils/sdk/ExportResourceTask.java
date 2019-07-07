package com.jxtb.batch.common.utils.sdk;

import com.jxtb.batch.common.exception.BizException;
import com.jxtb.batch.common.ftp.ScpBean;
import com.jxtb.batch.common.service.CommonConfigService;
import com.jxtb.batch.common.utils.FileTools;
import com.jxtb.batch.common.utils.SftpUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * Created by jxtb on 2019/7/3.
 */
public class ExportResourceTask implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(ExportResourceTask.class);
    private Resource[] resources;
    private String remoteDirEnum;
    private String sftpConfEnum;
    private String remoteFileName;
    //远程文件前缀
    private String remoteFileNamePrefix;
    //远程文件后缀
    private String remoteFileNameSuffix;
    //是否包括ok文件
    private boolean includeOkFile;
    private CommonConfigService commonConfigService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.info("开始导出文件");
        try{
            if(uploadFile()){
                return RepeatStatus.FINISHED;
            }
        }catch (Exception e){
            logger.error("uploadFile exception", e);
            throw e;
        }
        return null;
    }

    private boolean uploadFile() throws IOException{
        String fullFileName;
        for(Resource resource : resources){
            File file = resource.getFile();
            logger.info("文件名：{}，路径：{}", resource.getFilename(), file.getAbsolutePath());
//            if(file == null || !file.exists()){
//                throw new BizException("指定导出文件不存在");
//            }
            String fileName = file.getName();
            String path = file.getAbsolutePath();
            int pos = path.lastIndexOf(File.separator);
            String dir = path.substring(0, pos);
            File dirFile = new File(dir);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            if(StringUtils.isBlank(remoteFileName)){
                //远程文件名为空则用原始文件名
                fullFileName = fileName;
            }else{
                fullFileName = remoteFileName;
            }
            if(StringUtils.isNotBlank(remoteFileNamePrefix)){
                fullFileName = fullFileName + remoteFileNamePrefix;
            }
            if(StringUtils.isNotBlank(remoteFileNameSuffix)){
                fullFileName = fullFileName + remoteFileNameSuffix;
            }
            logger.info("上传文件名：{}", fullFileName);
            String remoteDir = commonConfigService.queryDir(remoteDirEnum);
            String remoteFilePath = remoteDir + fullFileName;
            ScpBean scpBean = commonConfigService.queryScpBean(sftpConfEnum);
            FileTools.uploadFileBySftp(path, remoteFilePath, scpBean);
            if(includeOkFile){
                logger.info("ok file create and upload");
                String remoteOkFileName = remoteFilePath + ".ok";
                SftpUtil.createOkFile(dir, fileName, ".ok");
                String okFIlePath = dir + File.separator + fileName + ".ok";
                FileTools.uploadFileBySftp(okFIlePath, remoteOkFileName, scpBean);
            }
        }
        return true;
    }

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public String getRemoteDirEnum() {
        return remoteDirEnum;
    }

    public void setRemoteDirEnum(String remoteDirEnum) {
        this.remoteDirEnum = remoteDirEnum;
    }

    public String getSftpConfEnum() {
        return sftpConfEnum;
    }

    public void setSftpConfEnum(String sftpConfEnum) {
        this.sftpConfEnum = sftpConfEnum;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getRemoteFileNamePrefix() {
        return remoteFileNamePrefix;
    }

    public void setRemoteFileNamePrefix(String remoteFileNamePrefix) {
        this.remoteFileNamePrefix = remoteFileNamePrefix;
    }

    public String getRemoteFileNameSuffix() {
        return remoteFileNameSuffix;
    }

    public void setRemoteFileNameSuffix(String remoteFileNameSuffix) {
        this.remoteFileNameSuffix = remoteFileNameSuffix;
    }

    public boolean isIncludeOkFile() {
        return includeOkFile;
    }

    public void setIncludeOkFile(boolean includeOkFile) {
        this.includeOkFile = includeOkFile;
    }

    public CommonConfigService getCommonConfigService() {
        return commonConfigService;
    }

    public void setCommonConfigService(CommonConfigService commonConfigService) {
        this.commonConfigService = commonConfigService;
    }
}
