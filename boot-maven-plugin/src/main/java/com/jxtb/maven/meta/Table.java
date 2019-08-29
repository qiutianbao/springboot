package com.jxtb.maven.meta;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据库表
 * Created by jxtb on 2019/6/26.
 */
public class Table {
    private String dbName;
    private String textName;
    private String description;
    private List<Column> columns = new ArrayList<Column>();
    private List<Column> primaryKeyColumns = new ArrayList<Column>();
    private List<List<Column>> indexs = new ArrayList<List<Column>>();
    private List<List<Column>> uniques = new ArrayList<List<Column>>();

    private FullyQualifiedJavaType javaClass;
    private FullyQualifiedJavaType javaKeyClass;

    /**
     * 额外属性，用于插件通信
     */
    private HashMap<Object, Object> additionalProperties = new HashMap<Object, Object>();

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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public void setPrimaryKeyColumns(List<Column> primaryKeyColumns) {
        this.primaryKeyColumns = primaryKeyColumns;
    }

    public List<List<Column>> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<List<Column>> indexs) {
        this.indexs = indexs;
    }

    public List<List<Column>> getUniques() {
        return uniques;
    }

    public void setUniques(List<List<Column>> uniques) {
        this.uniques = uniques;
    }

    public FullyQualifiedJavaType getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(FullyQualifiedJavaType javaClass) {
        this.javaClass = javaClass;
    }

    public FullyQualifiedJavaType getJavaKeyClass() {
        return javaKeyClass;
    }

    public void setJavaKeyClass(FullyQualifiedJavaType javaKeyClass) {
        this.javaKeyClass = javaKeyClass;
    }

    public HashMap<Object, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(HashMap<Object, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
