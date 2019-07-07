package com.jxtb.batch.common.utils.sdk;

import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * Created by jxtb on 2019/7/3.
 */
public class YakResource extends FileSystemResource{

    //改文件所对应的日期类型
    private String resourceDate;

    public YakResource(File file, String resourceDate) {
        super(file);
        this.resourceDate = resourceDate;
    }

    public YakResource(String path, String resourceDate) {
        super(path);
        this.resourceDate = resourceDate;
    }

    public String getResourceDate() {
        return resourceDate;
    }

    public void setResourceDate(String resourceDate) {
        this.resourceDate = resourceDate;
    }

    @Override
    public String toString() {
        return "计算获取日期为[" + resourceDate + "]的文件[" + super.getPath() + "]";
    }
}
