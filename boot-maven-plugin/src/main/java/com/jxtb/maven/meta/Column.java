package com.jxtb.maven.meta;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;

/**
 * Created by jxtb on 2019/6/26.
 */
public class Column {
    //由importer填写
    private String dbName;
    private String textName;
    private String description;
    private int length;
    private int scale;
    private boolean mandatory;
    private FullyQualifiedJavaType javaType;
    private boolean lob = false;
    private boolean identity = false;
    private boolean version = false;
    private String hint;
    private String temporal;
    private Domain domain;
    /**
     * 用于指定当前类型为简单类型，而不是在解析类时出现的数组、Collection等
     */
    private boolean simpleType = false;
    //由总控填写
    private String propertyName;
    private String id;
    private String dbType;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

    public void setJavaType(FullyQualifiedJavaType javaType) {
        this.javaType = javaType;
    }

    public boolean isLob() {
        return lob;
    }

    public void setLob(boolean lob) {
        this.lob = lob;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public boolean isSimpleType() {
        return simpleType;
    }

    public void setSimpleType(boolean simpleType) {
        this.simpleType = simpleType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}