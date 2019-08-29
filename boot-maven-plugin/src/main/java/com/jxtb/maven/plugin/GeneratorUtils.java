package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.Column;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.ibatis.ibator.api.dom.java.*;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxtb on 2019/6/27.
 */
public class GeneratorUtils {

    public static String dbName2ClassName(String dbName){
        String s = dbName;
        boolean allUpperCaseOrNumeric = true;
        for(char c : s.toCharArray()){
            if(c != '_' && CharUtils.isAsciiNumeric(c) && !CharUtils.isAsciiAlphaUpper(c)){
                allUpperCaseOrNumeric = false;
                break;
            }
        }
        if(allUpperCaseOrNumeric){
            //为应对Java类定义的情况，只有在全大写时才需要定义
            s = s.toLowerCase();
            s = WordUtils.capitalizeFully(s, new char[]{'_'});
            s = StringUtils.remove(s, '_');
        }
        if(!StringUtils.isAlpha(StringUtils.left(s, 1)))
            s = "_" + s;
        return s;
    }

    public static String dbName2PropertyName(String dbName){
        return WordUtils.uncapitalize(dbName2ClassName(dbName));
    }

    public static Field generateProperty(TopLevelClass clazz, FullyQualifiedJavaType fqit, String property, List<String> javadoc, boolean trimStrings, boolean keyColum, int order){
        clazz.addImportedType(fqit);

        Field field = new Field();
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setType(fqit);
        if(keyColum){
            field.addAnnotation("@AresKey");
        }
        if(javadoc != null && javadoc.size() > 0){
            field.addAnnotation("@AresDoc(value=\"" + javadoc.get(0) + "\",order=" + order + ")");
        }
        field.setName(property);
        clazz.addField(field);

        //getter
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(JavaBeansUtil.getGetterMethodName(field.getName(), field.getType()));
        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        sb.append(property);
        sb.append(";");
        method.addBodyLine(sb.toString());

        createJavaDoc(method, javadoc);

        clazz.addMethod(method);

        //setter
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(JavaBeansUtil.getSetterMethodName(property));
        method.addParameter(new Parameter(fqit, property));
        createJavaDoc(method, javadoc);

        if(trimStrings && fqit.equals(FullyQualifiedJavaType.getStringInstance())){
            sb.setLength(0);
            sb.append("this.");
            sb.append(property);
            sb.append(" = ");
            sb.append(property);
            sb.append(" == null ? null : ");
            sb.append(property);
            sb.append(".trim();");
            method.addBodyLine(sb.toString());
        }else{
            sb.setLength(0);
            sb.append("this.");
            sb.append(property);
            sb.append(" = ");
            sb.append(property);
            sb.append(":");
            method.addBodyLine(sb.toString());
        }
        clazz.addMethod(method);

        return field;
    }

    private static void createJavaDoc(Method method, List<String> javadoc) {
        if(javadoc != null){
            method.addJavaDocLine("/**");
            for(String line : javadoc){
                method.addJavaDocLine(" * <p>" + line + "<p>");
            }
            method.addJavaDocLine("*/");
        }
    }

    public static List<String> generatePropertyJavadoc(Column col){
        try{
            List<String> result = new ArrayList<String>();
            result.add(col.getTextName());
            String desc = col.getDescription();
            if(StringUtils.isNotBlank(desc)){
                BufferedReader br = new BufferedReader(new StringReader(desc));
                String line = br.readLine();
                while (line != null){
                    if(line.equals("///"))
                        break;
                    line = StringUtils.remove(line,"[[");
                    line = StringUtils.remove(line, "]]");
                    result.add(line);
                    line = br.readLine();
                }
            }
            return result;
        }catch (IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    public static FullyQualifiedJavaType forType(TopLevelClass topLevelClass, String type) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(type);
        topLevelClass.addImportedType(fqjt);
        return fqjt;
    }
}
