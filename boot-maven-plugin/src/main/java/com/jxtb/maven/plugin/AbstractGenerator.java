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
public class AbstractGenerator implements Generator{
    private Log logger;
    @Override
    public List<CompilationUnit> generateAdditionalClasses(Table table, DataBase dataBase) {
        return null;
    }

    @Override
    public List<CompilationUnit> generateAdditionalClasses(Table table) {
        return null;
    }

    @Override
    public List<GeneralFileContent> generateAdditionalClasses(DataBase dataBase) {
        return null;
    }

    @Override
    public List<GeneralFileContent> generateAdditionalFiles(Table table, DataBase dataBase) {
        return null;
    }

    @Override
    public void afterEntityGenerated(TopLevelClass entityClass, Table table) {

    }

    @Override
    public void afterKeyGenerated(TopLevelClass keyClass) {

    }

    @Override
    public void setLogger(Log log) {
        this.logger = log;
    }

}
