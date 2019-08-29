package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.Column;
import com.jxtb.maven.meta.Table;
import com.jxtb.maven.util.DataTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ibator.api.dom.java.*;

import java.text.MessageFormat;

/**
 * Created by jxtb on 2019/6/26.
 */
public class Entity2Map extends AbstractGenerator{

    public void afterEntityGenerated(TopLevelClass entityClass, Table table) {
        //建立covertToMap方法
        FullyQualifiedJavaType fqjtSerializable = new FullyQualifiedJavaType("java.io.Serializable");
        entityClass.addImportedType(fqjtSerializable);
        FullyQualifiedJavaType fqjtMap = new FullyQualifiedJavaType("java.util.Map");
        fqjtMap.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        fqjtMap.addTypeArgument(fqjtSerializable);
        entityClass.addImportedType(fqjtMap);

        Method to = new Method();
        to.setName("convertToMap");
        to.setVisibility(JavaVisibility.PUBLIC);
        to.setReturnType(fqjtMap);
        FullyQualifiedJavaType fqjtHahsMap = new FullyQualifiedJavaType("java.util.HashMap");
        fqjtHahsMap.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        fqjtHahsMap.addTypeArgument(fqjtSerializable);
        entityClass.addImportedType(fqjtHahsMap);
        to.addBodyLine("HashMap<String, Serializable> result = new HashMap<String, Serializable>();");

        for (Column col : table.getColumns()){
            entityClass.addImportedType(col.getJavaType());
            //普通类型
            to.addBodyLine(MessageFormat.format("map.put(\"{0}\", {0})", col.getPropertyName()));
        }
        to.addBodyLine("return map:");
        entityClass.addMethod(to);

        Method uf = new Method();
        uf.setName("updateFormMap");
        uf.setVisibility(JavaVisibility.PUBLIC);
        uf.addParameter(new Parameter(fqjtMap, "map"));

        setupSetFields(entityClass, table, uf, "this");

        entityClass.addMethod(uf);
    }

    private void setupSetFields(TopLevelClass clazz, Table table, Method method, String instanceName) {
        clazz.addImportedType(new FullyQualifiedJavaType(DataTypeUtils.class.getCanonicalName()));
        for(Column col : table.getColumns()){
            clazz.addImportedType(col.getJavaType());
            if(col.getJavaType().getShortName().equals("byte[]"))
                continue; //跳过blob
            method.addBodyLine(MessageFormat.format("if (map.cotainsKey(\"{0}\")) {1}.set{2}(DataTypeUtils.get{3}Value(map.get(\"{0}\"))));",
                    col.getPropertyName(),
                    instanceName,
                    StringUtils.capitalize(col.getPropertyName()),
                    col.getJavaType().getShortName()));
        }
    }

}
