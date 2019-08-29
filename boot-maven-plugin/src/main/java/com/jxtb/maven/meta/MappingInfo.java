package com.jxtb.maven.meta;

/**
 * Created by jxtb on 2019/6/26.
 */
public class MappingInfo {
    private String content = null;
    private String fileName = null;;

    public MappingInfo() {
        super();
    }

    public MappingInfo(String content, String fileName) {
        this.content = content;
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
