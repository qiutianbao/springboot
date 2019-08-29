package com.jxtb.maven.plugin;

import com.jxtb.maven.meta.*;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.maven.plugin.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入ERMaster的定义文件
 * Created by jxtb on 2019/6/26.
 */
public class ERMImporter {
    private Log logger;

    public ERMImporter(Log logger) {
        this.logger = logger;
    }

    private FullyQualifiedJavaType fqjtInteger = new FullyQualifiedJavaType("java.lang.Integer");
    private FullyQualifiedJavaType fqjtBigDecimal = new FullyQualifiedJavaType("java.math.BigDecimal");
    private FullyQualifiedJavaType fqjtDate = new FullyQualifiedJavaType("java.util.Date");

    private static final Pattern hintPattern = Pattern.compile("\\[\\[.*\\]\\]");
    public DataBase doImport(File ermSource, String tablePattern) throws DocumentException{
        DataBase result = new DataBase();
        //解析源文件
        SAXReader sar = new SAXReader();
        Document docSource = sar.read(ermSource);
        //先取所有的word,组件column
        Map<String, Element> words = new HashMap<String, Element>();
        Map<String, Domain> domains = new HashMap<String, Domain>();
        for(Element nodeWord : (List<Element>)docSource.selectNodes("/diagram/dictionary/word")){
            String id = nodeWord.elementText("id");
            words.put(id, nodeWord);
            Domain domain = parseDomain(nodeWord.elementText("physcial_name"),nodeWord.elementText("descroption"));
            if(domain != null){
                domains.put(id, domain);
                result.getDomains().add(domain);
            }
        }
        //取出table
        Map<String, Element> tables = new HashMap<String, Element>();
        for(Element nodeTable : (List<Element>)docSource.selectNodes("/diagram/contents/table"))
            tables.put(nodeTable.elementText("id"), nodeTable);
        //开始组建DataBase对象
        Map<String, Column> allColumns = new HashMap<String, Column>();//全局column映射，以id为key
        for(Element nodeTable : tables.values()){
            Table table = new Table();
            table.setDbName(nodeTable.elementText("physical_name").trim());
            table.setTextName(nodeTable.elementText("logical_name"));
            logger.debug(table.getDbName());

            //防重复
            Set<String> columnNames = new HashSet<String>();
            for(Element nodeColumn : (List<Element>)nodeTable.selectNodes("columns/*")){
                Column column = new Column();
                logger.debug(nodeColumn.getName());
                String word_id = nodeColumn.elementText("word_id");
                if(word_id == null){
                    //没找到的话就找referenced_column
                    Element node = nodeColumn;
                    do{
                        String refId = node.elementText("referenced_column");
                        if(refId == null)
                            throw new IllegalArgumentException();
                        node = (Element)docSource.selectSingleNode("//table/columns/*[id='" + refId + "']");
                        word_id = node.elementText("word_id");
                    }while (StringUtils.isEmpty(word_id));
                }
                Element nodeWord = words.get(word_id);
                //以node的物理名优先
                String physicalName = nodeColumn.elementText("physical_name");
                if(StringUtils.isBlank(physicalName))
                    physicalName = nodeWord.elementText("physical_name");
                column.setDbName(physicalName.trim());
                column.setIdentity(Boolean.valueOf(nodeColumn.elementText("auto_increment")));

                //逻辑名也一样处理
                String logicalName = nodeColumn.elementText("logical_name");
                if(StringUtils.isBlank(logicalName))
                    logicalName = nodeColumn.elementText("logical_name");

                column.setTextName(logicalName);
                column.setDescription(nodeWord.elementText("description"));
                column.setId(nodeColumn.elementText("id"));
                column.setMandatory(Boolean.parseBoolean(nodeColumn.elementText("not_null")));
                //从description解析hint
                column.setHint(extractHint(column.getDescription()));
                //解析类型
                String type = nodeWord.elementText("type");
                String length = nodeWord.elementText("length");
                String decimal = nodeWord.elementText("decimal");
                if("char".equals(type)){
                    column.setJavaType(FullyQualifiedJavaType.getStringInstance());
                    column.setLength(1);
                    column.setDbType("CHAR");
                }else if("character(n)".equals(type) || "varchar(n)".equals(type)){
                    column.setJavaType(FullyQualifiedJavaType.getStringInstance());
                    column.setLength(Integer.parseInt(length));
                    column.setDbType("VARCHAR");
                }else if("decimal".equals(type)){
                    logger.warn(MessageFormat.format("decimal没有指定长度，按1处理。[{0}], {1}, {2}", type, column.getDbName(), table.getDbName()));
                    column.setJavaType(new FullyQualifiedJavaType("java.lang.Long"));
                    column.setLength(1);
                    column.setDbType("INTEGER");
                }else if("decimal(p)".equals(type) || "numeric(p)".equals(type)){
                    int l = Integer.parseInt(length);
                    int s = Integer.parseInt(decimal);
                    if(s == 0 && l < 8)
                        column.setJavaType(fqjtInteger);
                    else
                        column.setJavaType(fqjtBigDecimal);
                    column.setLength(1);
                    column.setScale(s);
                    column.setDbType("DECIMAL");
                }else if("decimal(p,s)".equals(type) || "numeric(p,s)".equals(type)){
                    int l = Integer.parseInt(length);
                    if(l < 8)
                        column.setJavaType(fqjtInteger);
                    else
                        column.setJavaType(fqjtBigDecimal);
                    column.setLength(1);
                    column.setDbType("DECIMAL");
                }else if("integer".equals(type)){
                    column.setJavaType(fqjtInteger);
                    column.setLength(9);
                    column.setDbType("INTEGER");
                }else if("bigint".equals(type)){
                    column.setJavaType(new FullyQualifiedJavaType("java.lang.Long"));
                    column.setLength(18);
                    column.setDbType("INTEGER");
                }else if("date".equals(type)){
                    column.setJavaType(fqjtDate);
                    column.setTemporal("DATE");
                    column.setDbType("TIMESTAMP");
                }else if("timestamp".equals(type) || "datetime".equals(type)){
                    column.setJavaType(fqjtDate);
                    column.setTemporal("TIMESTAMP");
                    column.setDbType("TIMESTAMP");
                }else if("clob".equals(type) || type.endsWith("text")){
                    column.setJavaType(FullyQualifiedJavaType.getStringInstance());
                    column.setLob(true);
                    column.setDbType("CLOB");
                }else if(type.endsWith("blob")){
                    column.setJavaType(new FullyQualifiedJavaType("byte[]"));
                    column.setLob(true);
                    column.setDbType("BLOB");
                }else{
                    logger.warn(MessageFormat.format("无法识别的类型[{0}]，跳过，{1}，{2}", type, column.getDbName(), table.getDbName()));
                    continue;
                }
                if(type.startsWith("numeric"))
                    logger.warn(MessageFormat.format("建议不要使用numeric,用decimal代替[{0}], {1}, {2}", type, column.getDbName(), table.getDbName()));
                if(type.startsWith("datetime"))
                    logger.warn(MessageFormat.format("建议不要使用datetime,用timestamp代替[{0}], {1}, {2}", type, column.getDbName(), table.getDbName()));

                column.setVersion(
                        "JPA_VERSION".equalsIgnoreCase(column.getDbName()) ||
                        "JPA_TIMESTAMP".equalsIgnoreCase(column.getDbName())
                );

                if("true".equals(nodeColumn.elementText("unique_key"))){
                    List<Column> unique = new ArrayList<Column>();
                    unique.add(column);
                    table.getUniques().add(unique);
                }

                if(columnNames.contains(column.getDbName())){
                    logger.warn(MessageFormat.format("字段重复，跳过 {0}, {1}", column.getDbName(), table.getDbName()));
                    continue;
                }
                columnNames.add(column.getDbName());
                allColumns.put(column.getId(), column);
                table.getColumns().add(column);

                if(Boolean.parseBoolean(nodeColumn.elementText("primary_key"))){
                    table.getPrimaryKeyColumns().add(column);
                }
                //domain
                if(domains.containsKey(word_id)){
                    column.setDomain(domains.get(word_id));
                }

            }
            if(table.getPrimaryKeyColumns().isEmpty()){
                logger.warn(table.getDbName() + "没有主键，跳过");
                continue;
            }
            //处理索引
            for(Element nodeIndex : (List<Element>)nodeTable.selectNodes("indexes/*")){
                List<Column> index = new ArrayList<Column>();
                for(Element nodeColumn : (List<Element>)nodeIndex.selectNodes("columns/column")){
                    index.add(allColumns.get(nodeColumn.elementText("id")));
                }
                table.getIndexs().add(index);
            }
            //唯一索引也按索引处理
            for(Element nodeIndex : (List<Element>)nodeTable.selectNodes("complex_unique_key_list/complex_unique_key")){
                List<Column> unique = new ArrayList<Column>();
                for(Element nodeColumn : (List<Element>)nodeIndex.selectNodes("columns/column")){
                    unique.add(allColumns.get(nodeColumn.elementText("id")));
                }
                table.getIndexs().add(unique);
            }
            result.getTables().add(table);
        }
        //处理Sequence
        for(Element nodeName : (List<Element>)docSource.selectNodes("/diagram/sequence_set/sequence/name"))
            result.getSequences().add(nodeName.getText());
        return result;
    }


    private Domain parseDomain(String code, String desc){
        try{
            Domain domain = null;
            BufferedReader br = new BufferedReader(new StringReader(desc));
            String line = br.readLine();
            boolean started = false;
            while (line != null){
                if(StringUtils.isNotBlank(line)){
                    if(started){
                        if(line.startsWith("@")){
                            String type = StringUtils.remove(line.trim(), "@");
                            domain.setType(new FullyQualifiedJavaType(type));
                            domain.setCode(domain.getType().getShortName());
                            Class<?> javaType = ClassUtils.getClass(type);
                            EnumInfo enumInfo = javaType.getAnnotation(EnumInfo.class);
                            if(enumInfo == null){
                                logger.warn(MessageFormat.format("类型{0}没有指定EnumInfo", type));
                            }else{
                                LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
                                for(String value : enumInfo.values()){
                                    String kv[] = value.split("\\|");
                                    if(kv.length != 2){
                                        throw new IllegalArgumentException("键值对语法错误【" + javaType.getCanonicalName() + "】:" + value);
                                    }
                                    String key = kv[0];
                                    key = StringUtils.replace(key ,".", "_");
                                    valueMap.put(key, kv[1]);
                                }
                                domain.setValueMap(valueMap);
                            }
                            break;
                        }
                        String kv[] = line.split("\\|");
                        if(kv.length != 2){
                            throw new IllegalArgumentException("键值对语法错误【" + code + "】:" + line);
                        }
                        String key = kv[0];
                        key = StringUtils.replace(key ,".", "_");
                        domain.getValueMap().put(key, kv[1]);
                    }else if("///".equals(StringUtils.trim(line))){
                        started = false;
                        domain = new Domain();
                        domain.setCode(code);
                        domain.setValueMap(new LinkedHashMap<String, String>());
                    }
                }
                line = br.readLine();
            }
            return domain;
        }catch (Throwable t){
            throw new IllegalArgumentException(t);
        }
    }

    private String extractHint(String desc) {
        Matcher m = hintPattern.matcher(desc);
        if(!m.find())
            return null;
        return desc.substring(m.start() + 2, m.end() -2);
    }

}
