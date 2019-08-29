package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.DataBase;
import com.jxtb.maven.meta.Table;
import org.apache.ibatis.ibator.api.dom.java.CompilationUnit;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.maven.plugin.logging.Log;

import java.util.List;

/**
 * Created by jxtb on 2019/6/26.
 */
public interface Generator {

    List<CompilationUnit> generateAdditionalClasses(Table table, DataBase dataBase);

    List<CompilationUnit> generateAdditionalClasses(Table table);

    /**
     * 生成额外的文件
     * @param dataBase
     * @return key为文件名，value为文件内容
     */
    List<GeneralFileContent> generateAdditionalClasses(DataBase dataBase);

    /**
     * 生成额外的文件
     * @param dataBase
     * @return key为文件名，value为文件内容
     */
    List<GeneralFileContent> generateAdditionalFiles(Table table, DataBase dataBase);

    void afterEntityGenerated(TopLevelClass entityClass, Table table);

    void afterKeyGenerated(TopLevelClass keyClass);

    void setLogger(Log log);

}
