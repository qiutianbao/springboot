package com.jxtb.maven.plugin;

import com.jxtb.maven.annotations.AresDoc;
import com.jxtb.maven.annotations.AresKey;
import com.jxtb.maven.meta.Column;
import com.jxtb.maven.meta.DataBase;
import com.jxtb.maven.meta.MappingInfo;
import com.jxtb.maven.meta.Table;
import com.jxtb.maven.util.MappingUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.dom.java.*;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by jxtb on 2019/6/27.
 */
public class Mybatis_Db2Mojo extends AbstractMojo{

    private String basePackage;
    private String outputDirectory;
    private String versionField;
    private File sources[];
    private boolean trimStrings;
    private boolean useAutoTrimType;
    private String tablePattern;
    private MavenProject project;
    private List<Generator> generators = new ArrayList<Generator>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            project.addCompileSourceRoot(outputDirectory);
            Resource r = new Resource();
            ArrayList<String> in = new ArrayList<String>();
            in.add("**/client/**");
            in.add("**/shared/**");
            r.setDirectory(outputDirectory);
            r.setIncludes(in);
            project.addResource(r);

            List<DataBase> dataBases = new ArrayList<DataBase>();
            PDMImporter pdmImporter = new PDMImporter(getLog());
            ERMImporter ermImporter = new ERMImporter(getLog());
            for(File source : sources){
                getLog().info("处理源文件:" + source.getAbsolutePath());
                String ext = FilenameUtils.getExtension(source.getName());
                if("pdm".equals(ext)){
                    dataBases.add(pdmImporter.doImport(source, tablePattern));
                }else if("erm".equals(ext)){
                    dataBases.add(ermImporter.doImport(source, tablePattern));
                }else{
                    throw new MojoExecutionException("不支持的扩展名[" + ext + "]");
                }
            }
            //调用插件
            generators.add(new EqualsHashCode());
            generators.add(new Entity2Map());
            generators.add(new ToString());
            generators.add(new FillDefaultValues());
            for(Generator generator : generators){
                generator.setLogger(getLog());
            }
            for(DataBase db : dataBases){
                List<CompilationUnit> units = generateEntity(db);
                List<MappingInfo> unitsMap = generateMapping(db);
                List<GeneralFileContent> allFiles = new ArrayList<GeneralFileContent>();
                //先把内容生成处理，统一房到allFiles里
                for(CompilationUnit unit : units){
                    GeneratedJavaFile gif = new GeneratedJavaFile(unit, outputDirectory);
                    String filename = MessageFormat.format("{0}/{1}",
                            StringUtils.replace(gif.getTargetPackage(), ".", "/"),
                            gif.getFileName());
                    allFiles.add(new GeneralFileContent(filename, gif.getFormattedContent(),"UTF-8"));
                }
                //生成java文件
                for (GeneralFileContent file : allFiles){
                    File targetFile = new File(FilenameUtils.concat(outputDirectory, file.getFilename()));
                    FileUtils.writeStringToFile(targetFile, file.getContent(), file.getEncoding());
                }
                //生成mybatis映射文件
                for(MappingInfo info : unitsMap){
                    String outputMappingDirectory = outputDirectory.substring(0, outputDirectory.length() - 4) + "resources/mybatis-mapping";
                    File targetFile = new File(FilenameUtils.concat(outputMappingDirectory, info.getFileName()));
                    FileUtils.writeStringToFile(targetFile, info.getContent(), "UTF-8");
                }
            }
        }catch (Exception e){
            throw new MojoExecutionException("生成过程错误", e);
        }
    }

    private List<CompilationUnit> generateEntity(DataBase db) {
        List<CompilationUnit> generateFiles = new ArrayList<CompilationUnit>();
        Map<Table, TopLevelClass> generatingMap = new HashMap<Table, TopLevelClass>();
        //生成entity
        for(Table table : db.getTables()){
            table.setJavaClass(new FullyQualifiedJavaType(basePackage + "." + JavaBeansUtil.getCamelCaseString(table.getDbName(), true)));
            for(Column column : table.getColumns()){
                column.setPropertyName(JavaBeansUtil.getCamelCaseString(column.getDbName(), false));
                TopLevelClass entityClass = new TopLevelClass(table.getJavaClass());
                entityClass.setVisibility(JavaVisibility.PUBLIC);
                if(StringUtils.isNotBlank(table.getTextName())){
                    entityClass.addJavaDocLine("/**");
                    entityClass.addJavaDocLine(" * " + table.getTextName() + " " + table.getDbName());
                    entityClass.addJavaDocLine(" * @author boot-maven-plugin");
                    entityClass.addJavaDocLine(" */");
                }
                entityClass.addImportedType(new FullyQualifiedJavaType(AresKey.class.getCanonicalName()));
                entityClass.addImportedType(new FullyQualifiedJavaType(AresDoc.class.getCanonicalName()));
                entityClass.addSuperInterface(GeneratorUtils.forType(entityClass, Serializable.class.getCanonicalName()));

                Field serialField = new Field();
                serialField.setFinal(true);
                serialField.setStatic(true);
                serialField.setName("serialVersionUID");
                FullyQualifiedJavaType longType = new FullyQualifiedJavaType("long");
                serialField.setType(longType);
                serialField.setInitializationString("1L");
                serialField.setVisibility(JavaVisibility.PRIVATE);
                entityClass.addField(serialField);

                int order = 1;
                for(Column col : table.getColumns()){
                    GeneratorUtils.generateProperty(entityClass, col.getJavaType(), col.getPropertyName(), GeneratorUtils.generatePropertyJavadoc(col),
                            trimStrings, MappingUtil.isPrimaryKey(table.getPrimaryKeyColumns(), col), order ++);
                }
                for(Generator generator : generators){
                    generator.afterEntityGenerated(entityClass, table);
                }
                generateFiles.add(entityClass);
                generatingMap.put(table, entityClass);
            }
        }
        return generateFiles;
    }

    private List<MappingInfo> generateMapping(DataBase db) {
        List<MappingInfo> generatedFiles = new ArrayList<MappingInfo>();
        for(Table table : db.getTables()){
            MappingInfo mappingInfo = new MappingInfo();
            StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            sb.append("\n");
            sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
            sb.append("\n");
            sb.append("<mapper namespace=\"");
            //命名空间
            sb.append(basePackage + ".mapping." + JavaBeansUtil.getCamelCaseString(table.getDbName(), true) + "Mapper\">");
            sb.append("\n");
            sb.append("<resultMap id=\"BaseResultMap\" type=\"");
            sb.append(getEntityFullName(table) + "\" >\n");

            //映射语句
            List<Column> keyColumns = table.getPrimaryKeyColumns();
            Set<String> keyNames = new HashSet<String>();
            for(Column col : keyColumns){
                sb.append("    <id column=\"");
                sb.append(col.getDbName());
                sb.append("\" property=\"");
                sb.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
                sb.append("\" jdbcType=\"");
                sb.append(col.getDbType());
                sb.append("\"/>\n");
                keyNames.add(col.getDbName());
            }
            for(Column col : table.getColumns()){
                if(keyNames.contains(col.getDbName())){
                    continue;
                }
                sb.append("    <result column=\"");
                sb.append(col.getDbName());
                sb.append("\" property=\"");
                sb.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
                sb.append("\" jdbcType=\"");
                sb.append(col.getDbType());
                sb.append("\"/>\n");
            }
            sb.append("</resultMap>\n");

            MappingUtil.createTableFieldId(sb, table.getColumns(), table.getDbName());
            MappingUtil.createTableFieldDb2(sb, table.getColumns(), table.getDbName());

            //生成删除语句
            sb.append("<delete id=\"deleteByPrimaryKey\" parameterType=\"map\" >\n");
            sb.append("    delete from ");
            sb.append(table.getDbName());
            sb.append("\n");
            sb.append("     where  \n");
            processKeyColumns(sb, keyColumns);
            sb.append(" </delete>\n");

            //生成插入语句
            sb.append("<insert id=\"insert\" parameterType=\"");
            sb.append(getEntityFullName(table));
            if(getAutoIncrementKey(keyColumns) != null){
                sb.append("\"  useGeneratedKeys=\"true\" keyProperty=\"");
                sb.append(getAutoIncrementKey(keyColumns));
            }
            sb.append("\" >\n");
            sb.append("    insert into " + table.getDbName() + " ( ");
            sb.append(MappingUtil.createInsertStatement(table.getDbName()));
            sb.append(" )\n");
            sb.append("    values ( ");
            sb.append(createInsertStatementA(table.getColumns()));
            sb.append(" \n )");
            sb.append("\n  </insert>\n");

            //生成不能更新新空值的更新语句
            sb.append(" <update id=\"updateNotNullByPrimaryKey\" parameterType=\"");
            sb.append(getEntityFullName(table));
            sb.append("\">\n");
            sb.append("     update ");
            sb.append(table.getDbName());
            sb.append(" \n");
            sb.append("     <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" > \n");
            sb.append(MappingUtil.createUpdateStatement(table.getColumns(), keyNames));
            sb.append("     </trim>");
            sb.append("\n      where  \n");
            MappingUtil.processUpdateKeyColumnNoVersion(sb, keyColumns, table.getColumns());
            sb.append("</update>\n");

            //生成更新语句
            sb.append(" <update id=\"updateByPrimaryKey\" parameterType=\"");
            sb.append(getEntityFullName(table));
            sb.append("\">\n");
            sb.append("    update ");
            sb.append(table.getDbName());
            sb.append(" \n");
            sb.append("     <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" > \n");
            sb.append(MappingUtil.createUpdateNullableStatement(table.getColumns(), keyNames ,versionField));
            sb.append("     </trim>");
            sb.append("\n      where  \n");
            MappingUtil.processUpdateKeyColumns(sb, keyColumns, table.getColumns(), versionField);
            sb.append("</update>\n");

            //生成查询语句(通过主键)
            sb.append("  <select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"map\" >");
            sb.append("      select ");
            sb.append(MappingUtil.createSelectStatement(table.getDbName()));
            sb.append("      \n      from ");
            sb.append(table.getDbName());
            sb.append("\n         where  \n");
            processKeyColumns(sb, keyColumns);
            sb.append(" </select>\n");

            //生成查询语句
            sb.append("  <select id=\"selectAll\" resultMap=\"BaseResultMap\" parameterType=\"map\" >");
            sb.append("      select ");
            sb.append(MappingUtil.createSelectStatement(table.getDbName()));
            sb.append("      \n      from ");
            sb.append(table.getDbName());
            sb.append("\n         where 1=1 \n");
            sb.append(MappingUtil.createConditionalStatement(table.getColumns()));
            sb.append(MappingUtil.createOrderList());
            sb.append("\n </select>");

            //查询ID
            MappingUtil.createIdList(sb, table.getDbName(), table.getColumns());
            sb.append("\n</mapper>");
            mappingInfo.setContent(sb.toString());
            mappingInfo.setFileName(JavaBeansUtil.getCamelCaseString(table.getDbName(), true) + "Mapper.xml");

            generatedFiles.add(mappingInfo);
        }
        return generatedFiles;
    }

    private String getEntityFullName(Table table) {
        return basePackage + "." + JavaBeansUtil.getCamelCaseString(table.getDbName(), true);
    }

    private void processKeyColumns(StringBuilder sb, List<Column> keyColumns) {
        int size = keyColumns.size();
        int i = 1;
        for(Column col : keyColumns){
            sb.append("      ");
            sb.append(col.getDbName());
            sb.append(" = #{");
            sb.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            sb.append(",jdbcType=");
            sb.append(col.getDbType());
            sb.append("}\n");
            if(i++ < size){
                sb.append("     and  ");
            }
        }
    }

    private String getAutoIncrementKey(List<Column> keyColumns) {
        for(Column col : keyColumns){
            if(col.isIdentity()){
                return JavaBeansUtil.getCamelCaseString(col.getDbName() ,false);
            }
        }
        return null;
    }


    private String createInsertStatementA(List<Column> keyColumns) {
        StringBuilder temp = new StringBuilder("");
        int i = 0;
        int size = keyColumns.size();
        for(Column col : keyColumns){
            if(col.isIdentity()){
                i++;
                continue;
            }
            temp.append(" #{");
            temp.append(JavaBeansUtil.getCamelCaseString(col.getDbName(), false));
            temp.append(",jdbcType=");
            temp.append(col.getJavaType());
            temp.append(" }");
            if(i++ < size){
                temp.append(" ,");
            }
            if(i % 4 == 0){
                temp.append("\n         ");
            }
        }
        return temp.toString();
    }


    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getVersionField() {
        return versionField;
    }

    public void setVersionField(String versionField) {
        this.versionField = versionField;
    }

    public File[] getSources() {
        return sources;
    }

    public void setSources(File[] sources) {
        this.sources = sources;
    }

    public boolean isTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public boolean isUseAutoTrimType() {
        return useAutoTrimType;
    }

    public void setUseAutoTrimType(boolean useAutoTrimType) {
        this.useAutoTrimType = useAutoTrimType;
    }

    public String getTablePattern() {
        return tablePattern;
    }

    public void setTablePattern(String tablePattern) {
        this.tablePattern = tablePattern;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public void setGenerators(List<Generator> generators) {
        this.generators = generators;
    }
}
