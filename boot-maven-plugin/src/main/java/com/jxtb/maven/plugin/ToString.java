package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.Table;
import org.apache.ibatis.ibator.api.dom.java.*;

import java.text.MessageFormat;

/**
 * Created by jxtb on 2019/6/27.
 */
public class ToString extends AbstractGenerator{

    public void afterEntityGenerated(TopLevelClass entityClass, Table table) {
        Method toString = new Method();
        entityClass.addMethod(toString);
        toString.setName("toString");
        toString.setVisibility(JavaVisibility.PUBLIC);
        toString.setReturnType(FullyQualifiedJavaType.getStringInstance());
        toString.addAnnotation("@Override");
        toString.addBodyLine("return getClass().getName() + \"@\" + Integer.toHexString(hashCode())+\"[\"+");
        int i = 1;
        for(Field field : entityClass.getFields()){
            if(i > 1){
                i ++;
                toString.addBodyLine(MessageFormat.format("\",{0}=\"+{0}+", field.getName()));
            }
            toString.addBodyLine(MessageFormat.format("\",{0}=\"+{0}+", field.getName()));
        }
        toString.addBodyLine("\"]\":");
    }

}
