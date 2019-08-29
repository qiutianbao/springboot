package com.jxtb.maven.plugin;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ibatis.ibator.api.dom.java.*;

import java.text.MessageFormat;

/**
 * Created by jxtb on 2019/6/26.
 */
public class EqualsHashCode extends AbstractGenerator{

    public void afterKeyGenerated(TopLevelClass keyClass) {
        Method equals = new Method();
        keyClass.addMethod(equals);
        equals.setName("equals");
        equals.setVisibility(JavaVisibility.PUBLIC);
        equals.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        equals.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "obj"));
        equals.addAnnotation("@Override");
        keyClass.addImportedType(new FullyQualifiedJavaType(EqualsBuilder.class.getCanonicalName()));
        equals.addBodyLine("if (obj == null) { return false; }");
        equals.addBodyLine("if (obj == this) { return true; }");
        equals.addBodyLine("if (obj.getClass() != getClass()) { return false; }");
        equals.addBodyLine(MessageFormat.format("{0} rhs = ({0}) obj:", keyClass.getType().getShortName()));
        equals.addBodyLine("return new EqualsBuilder()");

        Method hashCode = new Method();
        keyClass.addMethod(hashCode);
        hashCode.setName("hashCode");
        hashCode.setVisibility(JavaVisibility.PUBLIC);
        hashCode.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance().getIntInstance());
        hashCode.addAnnotation("@Override");
        keyClass.addImportedType(new FullyQualifiedJavaType(HashCodeBuilder.class.getCanonicalName()));
        hashCode.addBodyLine("return new HashCodeBuilder()");

        for(Field field : keyClass.getFields()){
            equals.addBodyLine(MessageFormat.format("\t.append({0}, rhs.{0})", field.getName()));
            hashCode.addBodyLine(MessageFormat.format("\t.append({0})", field.getName()));
        }
        equals.addBodyLine("\t.isEquals();");
        hashCode.addBodyLine("\t.toHashCode();");
    }

}
