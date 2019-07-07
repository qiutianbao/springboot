package com.jxtb.batch.common.writer;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPInputStream;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.jxtb.batch.common.reader.FileHeader;
import com.jxtb.batch.common.utils.sdk.DESUtils;
import com.jxtb.batch.common.utils.sdk.cstruct.SStruct;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.util.ExecutionContextUserSupport;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 写文件
 * Created by jxtb on 2019/7/3.
 */
public class FileItemWriter <H extends FileHeader, D> extends ExecutionContextUserSupport implements
        ResourceAwareItemWriterItemStream<D>, BeanNameAware{

    private static final String STATE_KEY = "state";
    private static final String LAST_IP_KEY = "lastIp";
    private static final String RECORD_COUNT_KEY = "recordCount";
    private static final int MAX_LINE_LENGTH = 4096;
    private static final int FILE_HEADER_LENGTH = 22;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Resource resource;
    private FileChannel fileChannel;
    private RandomAccessFile outputRandomAccessFile;
    private File outputTempFile;
    private State state;
    private Class<H> fileHeaderClass;
    private Class<D> fileDetailClass;
    protected String newLine = "\n";
    private File filePath;
    private String login_user; //用于scp
    private String login_passwd; //用于scp
    private int record_count = 0;
    private int maxLineLength = MAX_LINE_LENGTH;

    /**
     * 使用CStruct输出的编码
     */
    private String charset = "UTF-8";

    /**
     * 内容分隔符
     */
    private String seperator = "@@";
    private SStruct<D> detailStruct;
    private SStruct<H> headerStruct;
    private ByteBuf detailBuffer;
    private String detailLable;

    @PostConstruct
    public void init(){
        detailStruct = new SStruct<D>(fileDetailClass, charset, seperator);
        if(null != fileHeaderClass){
            headerStruct = new SStruct<H>(fileHeaderClass, charset, seperator);
        }
        detailBuffer = Unpooled.copiedBuffer(new byte[maxLineLength]);
        try{
            filePath = resource.getFile();
        }catch (IOException e){
            ;
        }
        DESUtils des = DESUtils.getInstance();
        try{
            login_passwd = des.decrypt(login_passwd);
        }catch (Exception e){
            ;
        }
    }

    @Override
    public void setResource(Resource resource) {
        try{
            logger.info("注入的文件名为：{}",resource.getFile().getAbsolutePath());
        }catch (IOException e){
            ;
        }
        this.resource = resource;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        String key = getKey(STATE_KEY);
        if(executionContext.containsKey(key)){
            //减少java对象的串行化，State改为字符串，竖线分隔，以便运行维护
            state = State.parseState((String)executionContext.get(key));
        }else{
            state = new State();
        }
        String recordCountKey = getKey(RECORD_COUNT_KEY);
        if(executionContext.containsKey(recordCountKey)){
            record_count = executionContext.getInt(recordCountKey); //记录数
        }
        try{
            isLastRunner(executionContext); //断点续批校验文件
        }catch (Exception e){
            logger.error("打开文件出错", e);
            throw new ItemStreamException(e);
        }
    }

    private void isLastRunner(ExecutionContext executionContext) {
        String key = getKey(LAST_IP_KEY);
        if(!executionContext.containsKey(key)){
            return;
        }
        String lastIp = executionContext.getString(key);
        String localIp = NetUtils.getLocalHost();
        if(localIp.equals(lastIp)){
            logger.info("上次跑的是本机");
            return;
        }
        logger.info("上次跑批IP地址：{}", lastIp);
        downloanFileFromOtherMache(lastIp);
    }

    private void downloanFileFromOtherMache(String host) {
        Connection con = new Connection(host);
        FileOutputStream fous = null;
        try{
            con.connect();
            boolean isAuthed = con.authenticateWithPassword(login_user, login_passwd);
            logger.info("isAuthed===={}", isAuthed);
            SCPClient scpClient = con.createSCPClient();
            scpClient.setCharset(charset);
            logger.info("开始下载文件{}", filePath.getCanonicalPath() + ".ares");
            SCPInputStream in = scpClient.get(filePath.getCanonicalPath() + ".ares");
            byte buffer[] = new byte[1024];
            int len;
            fous = new FileOutputStream(filePath + ".ares");
            while ((len = in.read(buffer)) >= 0){
                logger.info("read byte {}", len);
                fous.write(buffer);
            }
        }catch (Exception e){
            logger.error("scp错误：", e);
        }finally {
            if(fous != null){
                try{
                    fous.close();
                }catch (Exception e){
                    ;
                }
            }
            if(con != null){
                con.close();
            }
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        Assert.notNull(state);
        try{
            if(fileChannel != null && fileChannel.isOpen()){
                state.lastPosition = fileChannel.position();
                //存入context,减少java对象的串行化，State改为字符串，竖线分隔，以便运行维护
                executionContext.put(getKey(STATE_KEY), state.convertToStr());
            }
            executionContext.put(getKey(LAST_IP_KEY), NetUtils.getLocalHost());
            executionContext.putInt(getKey(RECORD_COUNT_KEY), record_count);
        }catch (IOException e){
            throw new ItemStreamException("更新文件状态出错", e);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        try{
            if(fileChannel != null && fileChannel.isOpen() && null != fileHeaderClass){
                //最终更新文件头
                fileChannel.position(0);
                H header = fileHeaderClass.newInstance();
                header.detailSize = 0;
                header.filename = resource.getFilename();
                header.recordCount = record_count;
                ByteBuffer buffer = ByteBuffer.allocate(FILE_HEADER_LENGTH + resource.getFilename().getBytes(charset).length + newLine.getBytes().length);
                headerStruct.writeByBuffer(header, buffer);
                buffer.put(newLine.getBytes());
                buffer.flip();
                fileChannel.write(buffer);
            }
        }catch (IllegalAccessException e){
            throw new ItemStreamException("写入文件出错", e);
        }catch (InstantiationException e){
            throw new ItemStreamException("写入文件出错", e);
        }catch (IOException e){
            throw new ItemStreamException("文件IO出错", e);
        }finally {
            IOUtils.closeQuietly(fileChannel);
            IOUtils.closeQuietly(outputRandomAccessFile);
        }
        if(StepSynchronizationManager.getContext().getStepExecution().getExitStatus().equals(ExitStatus.COMPLETED)){
            //成功处理，改文件名为最终文件
            try{
                if(resource.exists()){
                    logger.warn("输出五年级[{}]已存在，将被删除。", resource.getURL());
                    resource.getFile().delete();
                }
                logger.info("路径：{},文件：{}", resource.getFile(), filePath);
                outputTempFile.renameTo(filePath);
            }catch (IOException e){
                throw new ItemStreamException("最终文件改名失败", e);
            }
        }
    }

    @Override
    public void write(List<? extends D> items) throws Exception {
        for(D item : items){
            if(item instanceof Iterable){
                //加入对Iterable类型的Item支持，其实这么写是违反泛型的语义的。
                //这样List<? extends D> items就是错的，但为了开发方便，就这么处理了
                for(D itemitem : (Iterable<D>)item){
                    doWriteItem(itemitem);
                }
            }else{
                doWriteItem(item);
            }
        }
    }

    private void doWriteItem(D item) throws IOException{
        detailBuffer.clear();
        detailStruct.writeSperatorByBuffer(item, detailBuffer);
        detailBuffer.writeBytes(newLine.getBytes());
        fileChannel.write(detailBuffer.nioBuffer());
        record_count ++;
    }

    @Override
    public void setBeanName(String name) {
        setName(name);
    }

    private static class State implements Serializable{
        private long lastPosition = 0;
        /**
         * 将State对象转为字符串
         * 减少java对象的串行化，State改为字符串，竖线分隔，以便运行维护
         */
        private String convertToStr(){
            return lastPosition + "";
        }
        /**
         * 将State对象转为字符串
         * 减少java对象的串行化，State改为字符串，竖线分隔，以便运行维护
         */
        private static State parseState(String value){
            State state = new State();
            if(null == state){
                return state;
            }
            state.lastPosition = Long.parseLong(value.trim());
            return state;        }
    }

    public Class<H> getFileHeaderClass() {
        return fileHeaderClass;
    }

    public void setFileHeaderClass(Class<H> fileHeaderClass) {
        this.fileHeaderClass = fileHeaderClass;
    }

    public Class<D> getFileDetailClass() {
        return fileDetailClass;
    }

    public void setFileDetailClass(Class<D> fileDetailClass) {
        this.fileDetailClass = fileDetailClass;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public String getDetailLable() {
        return detailLable;
    }

    public void setDetailLable(String detailLable) {
        this.detailLable = detailLable;
    }

    public int getMaxLineLength() {
        return maxLineLength;
    }

    public void setMaxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }
}
