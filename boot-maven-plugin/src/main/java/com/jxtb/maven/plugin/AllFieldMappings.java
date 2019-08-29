package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.Column;
import com.jxtb.maven.meta.DataBase;
import com.jxtb.maven.meta.Table;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ibator.api.dom.java.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxtb on 2019/6/26.
 */
public class AllFieldMappings extends AbstractGenerator{
    private String targetPackage;
    private static final String NAME_PREFIX = "M";

    public AllFieldMappings(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    @Override
    public List<CompilationUnit> generateAdditionalClasses(Table table, DataBase dataBase) {
        FullyQualifiedJavaType fqjEntity = table.getJavaClass();
        TopLevelClass clazz = new TopLevelClass(new FullyQualifiedJavaType(targetPackage + "." + NAME_PREFIX + fqjEntity.getShortName()));
        clazz.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType fqjtQ = new FullyQualifiedJavaType(fqjEntity.getPackageName() + "Q" + fqjEntity.getShortName());
        clazz.addImportedType(fqjtQ);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setStatic(true);
        method.setName("getAllMappings");

        FullyQualifiedJavaType fqjMap = new FullyQualifiedJavaType("java.util.Map");
        FullyQualifiedJavaType fqjExpression = new FullyQualifiedJavaType("com.mysema.query.types.Expression");
        fqjMap.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        fqjMap.addTypeArgument(fqjExpression);
        clazz.addImportedType(fqjMap);
        clazz.addImportedType(fqjExpression);
        method.setReturnType(fqjMap);

        clazz.addImportedType(new FullyQualifiedJavaType("java.util.HashMap"));
        method.addBodyLine("HashMap<String, Expression> result = new HashMap<String, Expression>();");
        method.addBodyLine(MessageFormat.format("{0} q = {0}.{1}:", fqjtQ.getShortName(), StringUtils.uncapitalize(fqjEntity.getShortName())));

        for(Column col : table.getColumns())
            method.addBodyLine(MessageFormat.format("result.put(\"{0}\", q.{0});", col.getPropertyName()));
        method.addBodyLine("return result.");
        clazz.addMethod(method);

        List<CompilationUnit> result = new ArrayList<CompilationUnit>();
        result.add(clazz);
        return result;
    }

}
