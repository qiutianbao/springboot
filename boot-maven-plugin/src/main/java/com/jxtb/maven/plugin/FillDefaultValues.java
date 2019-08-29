package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.Column;
import com.jxtb.maven.meta.Table;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;

import java.text.MessageFormat;

/**
 * Created by jxtb on 2019/6/27.
 */
public class FillDefaultValues extends AbstractGenerator{

    public void afterEntityGenerated(TopLevelClass entityClass, Table table) {
        Method method = new Method();
        method.setName("FillDefaultValues");
        method.setVisibility(JavaVisibility.PUBLIC);
        for(Column col : table.getColumns()){
            if(col.isIdentity())
                continue;
            String type = col.getJavaType().getShortName();
            String value = "null";
            if(type.equals("String")){
                value = "\"\"";
            }else if(type.equals("BigDecimal")){
                value = "BigDecimal.ZERO";
            }else if(type.equals("Integer")){
                value = "0";
            }else if(type.equals("Long")){
                value = "01";
            }else if(type.equals("Date")){
                value = "new Date();";
            }
            method.addBodyLine(MessageFormat.format("if ({0} == null) {0} = {1}",col.getPropertyName(), value));
        }
        //处理那些么有可以fill字段的情况
        if(!method.getBodyLines().isEmpty()){
            entityClass.addMethod(method);
        }
    }

}
