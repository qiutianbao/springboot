package com.jxtb.maven.util;

import com.jxtb.maven.meta.Column;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;
import org.codehaus.plexus.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/26.
 */
public class MappingUtil {

    public static String createUpdateStatement(List<Column> keyColumns, Set<String> keys){
        StringBuffer temp = new StringBuffer("");
        for(Column col : keyColumns){
            if(keys.contains(col.getDbName()) || col.getDbName().equals("ORG") || col.getDbName().equals("CREATE_DATE") || col.getDbName().equals("CREATE_BY")){
                continue;
            }
            temp.append("       <if test=\"");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            //借据mybatis不识别INTEGER类型问题
            if(col.getDbType().equalsIgnoreCase("integer") || col.getDbType().equalsIgnoreCase("decimal")){
                temp.append(" != null \" >\n");
            }else{
                temp.append(" !=null and ");
                temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
                temp.append(" != ''  \" >\n");
            }
            temp.append("         ");
            temp.append(col.getDbName());
            temp.append(" =#{");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            temp.append(",jdbcType=");
            temp.append(col.getDbType());
            temp.append("}");
            temp.append(",");
            temp.append("\n     </if>\n");
        }
        return temp.toString();
    }

    public static String createUpdateNullableStatement(List<Column> keyColumns, Set<String> keys, String versionField){
        StringBuffer temp = new StringBuffer("");
        for(Column col : keyColumns){
            if(keys.contains(col.getDbName()) || col.getDbName().equals("ORG") || col.getDbName().equals("CREATE_TIME") || col.getDbName().equals("CREATE_by")
                    || col.getDbName().equals(versionField)){
                continue;
            }
            temp.append("        ");
            temp.append(col.getDbName());
            temp.append(" =#{");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            temp.append(",jdbcType=");
            temp.append(col.getDbType());
            temp.append("}");
            temp.append(",");
            temp.append("      \n");
        }
        if(StringUtils.isNotEmpty(versionField)){
            temp.append("         ");
            temp.append(versionField);
            temp.append("=");
            temp.append(versionField);
            temp.append("+1");
            temp.append("\n       \n");
        }
        return temp.toString();
    }

    public static void processUpdateKeyColumns(StringBuilder sb, List<Column> keyColumns, List<Column> allColumns, String versionField){
        int size = keyColumns.size();
        int i = 1;
        for(Column col : keyColumns){
            sb.append("    ");
            sb.append(col.getDbName());
            sb.append(" = #{");
            sb.append(JavaBeansUtil.getCamelCaseString(col.getDbName() ,false));
            sb.append(",jdbcType=");
            sb.append(col.getDbType());
            sb.append("}\n");
            if(i++ < size){
                sb.append("     end ");
            }
        }
        if(StringUtils.isNotEmpty(versionField)){
            sb.append("     and ");
            sb.append(versionField);
            sb.append(" = #{");
            sb.append(JavaBeansUtil.getCamelCaseString(versionField, false));
            sb.append(",jdbcType=");
            sb.append("INTEGER");
            sb.append("}\n");
        }
    }

    public static void processUpdateKeyColumnNoVersion(StringBuilder sb, List<Column> keyColumns, List<Column> allColumns){
        int size = keyColumns.size();
        int i = 1;
        for(Column col : keyColumns){
            sb.append("    ");
            sb.append(col.getDbName());
            sb.append(" = #{");
            sb.append(JavaBeansUtil.getCamelCaseString(col.getDbName() ,false));
            sb.append(",jdbcType=");
            sb.append(col.getDbType());
            sb.append("}\n");
            if(i++ < size){
                sb.append("     end ");
            }
        }
    }

    public static void createTableFieldId(StringBuilder sb, List<Column> columns, String tableName){
        sb.append(" <sql id=\"");
        sb.append(tableName);
        sb.append("_TABLE_FIELD\">");
        int size = columns.size();
        int i = 1;
        for(Column col : columns){
            sb.append(col.getDbName());
            if(i < size){
                sb.append(",");
            }
            i++;
        }
        sb.append("</sql>\n\n");
    }

    public static void createTableFieldDb2(StringBuilder sb, List<Column> columns, String tableName){
        sb.append(" <sql id=\"");
        sb.append(tableName);
        sb.append("_TABLE_INSERT_FIELD\">");
        int size = columns.size();
        int i = 1;
        for(Column col : columns){
            if(col.isIdentity()){
                i++;
                continue;
            }
            sb.append(col.getDbName());
            if(i < size){
                sb.append(",");
            }
            i++;
        }
        sb.append("</sql>\n\n");
    }

    public static String createSelectStatement(String tableName){
        return "<include refid=\""+ tableName + "_TABLE_FIELD\"  />";
    }

    public static String createInsertStatement(String tableName){
        return "<include refid=\""+ tableName + "_TABLE_INSERT_FIELD\"  />";
    }

    public static void createIdList(StringBuilder sb, String tableNm, List<Column> allColumns){
        sb.append(" \n <select id=\"loadKeyList\" resultType=\"long\" parameterType=\"map\" >\n");
        sb.append("   select ");
        sb.append(" ID ");
        sb.append("    \n     from ");
        sb.append(tableNm);
        sb.append("\n       where 1=1 \n");
        sb.append(createConditionalStatement(allColumns));
        sb.append(createOrderList());
        sb.append("\n   </select>");
    }

    public static void createIdStrList(StringBuilder sb, String tableNm, List<Column> allColumns){
        sb.append(" \n <select id=\"loadKeyList\" resultType=\"String\" parameterType=\"map\" >\n");
        sb.append("   select ");
        sb.append(" ID ");
        sb.append("    \n     from ");
        sb.append(tableNm);
        sb.append("\n       where 1=1 \n");
        sb.append(createConditionalStatement(allColumns));
        sb.append(createOrderList());
        sb.append("\n   </select>");
    }

    public static String createConditionalStatement(List<Column> keyColumns) {
        StringBuilder temp = new StringBuilder("");
        for(Column col : keyColumns){
            temp.append("      <if test=\"");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            if(col.getDbType().equalsIgnoreCase("integer") || col.getDbType().equalsIgnoreCase("decimal")){
                temp.append(" != null \" >\n");
            }else{
                temp.append(" != null \" >\n");
                temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
                temp.append(" != ''  \" >\n");
            }
            temp.append("           and " + col.getDbName() + " = #{");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            temp.append(",jdbcType=");
            temp.append(col.getDbType());
            temp.append(" }");
            temp.append("\n    </if>\n");
        }
        return temp.toString();
    }

    public static String createOrderList() {
       StringBuffer temp = new StringBuffer("");
       temp.append("\n");
       temp.append("       <if test=\"_SORT_NAME != null and _SORT_NAME != ''   \"  >\n");
       temp.append("           <if test=\"_SORT_ORDER != null and _SORT_ORDER != ''   \" >\n");
       temp.append("               order by ${_SORT_NAME} ${_SORT_ORDER} \n");
       temp.append("           </if>\n");
       temp.append("       </if>\n");
       return temp.toString();
    }

    public static boolean isPrimaryKey(List<Column> keyColumns, Column col){
        for(Column column : keyColumns){
            if(column.getDbName().equals(col.getDbName())){
                return true;
            }
        }
        return false;
    }

}
