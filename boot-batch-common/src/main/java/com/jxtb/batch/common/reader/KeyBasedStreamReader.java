package com.jxtb.batch.common.reader;

import com.jxtb.batch.common.able.MasterStepExceptionAqwre;
import com.jxtb.batch.common.utils.redis.CacheOperation;
import com.jxtb.batch.common.utils.str.IDWorker;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 分页从数据库中读取数据
 * Created by jxtb on 2019/6/28.
 */
public abstract class KeyBasedStreamReader<INFO>
        extends AbstractItemCountingItemStreamItemReader<INFO> implements ItemStreamReader<INFO>, Partitioner, BeanNameAware, MasterStepExceptionAqwre {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> allkeys;
    private final String KEY_CONTEXT_KEY = "keyContextId";
    private final String KEY_SIZE = "keySize";
    private int keySize;
    private String cacheKey = "partition:key" + this.getClass().getSimpleName();
    @Autowired
    private CacheOperation cacheOperation;
    private int minPartitionSize = 20;

    /**
     * 默认不限制分片大小
     */
    private int maxPartitionSize = Integer.MAX_VALUE;
    private StepExecution masterStepException;
    protected abstract List<String> loadKeys();
    protected abstract INFO loadItemByKey(String key);

    /**
     * 超时时间
     */
    private static int TIMEOUT_SECOND = 3600 * 24 * 2;

    @Override
    public void setMasterStepExceptionAware(StepExecution stepException) {

    }

    @Override
    protected INFO doRead() throws Exception {
        int currentItemCount = getCurrentItemCount();
        logger.debug("当前主键索引：{}", currentItemCount);
        if(currentItemCount > keySize -1){
            return null;
        }
        String key = allkeys.get(currentItemCount);
        return loadItemByKey(key);
    }

    @Override
    protected void doOpen() throws Exception {
        //allKeys应该在beforeStep里加载或直接从ExecutionContext读取
        keySize = allkeys.size();
        logger.info("主键规模[{}]", keySize);
    }

    @Override
    protected void doClose() throws Exception {
        //清空缓存，以防万一
        allkeys = null;
        keySize = 0;
    }

    @Override
    public void setBeanName(String name) {
        //默认使用bean id 作为name
        setName(name);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        //仅为打日志
        super.update(executionContext);
        logger.info("已处理到游标的第{}页", getCurrentItemCount());
    }

    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        logger.info("跳转到{}开始的索引", itemIndex);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize){
        Map<String, ExecutionContext> result = new TreeMap<>(); //为了排序，使用TreepMap
        List<String> keys;
        if(masterStepException == null || masterStepException.getExecutionContext().containsKey(KEY_CONTEXT_KEY)){
            //用于支持分区
            keys = loadKeys();
        }else{
            //说明是slave上的片段
            String redisKey = masterStepException.getExecutionContext().getString(KEY_CONTEXT_KEY);
            keys = stringToList(cacheOperation.get(redisKey));
        }
        //取文件大小计算网络规模
        int total = keys.size();
        int partitionSize = total / gridSize + 1; //这里加1是为了避免出现因为整除而漏项的情况
        partitionSize = Math.max(partitionSize, minPartitionSize);
        partitionSize = Math.min(partitionSize, maxPartitionSize);

        logger.error("总记录数[{}],网络数[{}],网络大小[{}]", total, gridSize, partitionSize);

        //开始分partition,注意最后一个partition不要漏行
        int rest = total;
        int i = 0;
        while (rest > 0){
            ExecutionContext ac =new ExecutionContext();
            ArrayList<String> subList = new ArrayList<>(keys.subList(i * partitionSize, Math.min((i + 1) * partitionSize, total))); //动态subList不可串行化
            ac.putInt(KEY_SIZE, subList.size());
            String redisKey = cacheKey + ":" + IDWorker.getInstance().nextId();
            //设置超时时间为2天
            cacheOperation.put(redisKey, listToString(subList), TIMEOUT_SECOND);
            ac.put(KEY_CONTEXT_KEY, redisKey);
            result.put(MessageFormat.format("part{0,number,000}", i), ac);
            rest -= partitionSize;
            i++;
        }
        int currentSize = result.size();
        if(currentSize == 0){
            ExecutionContext ec = new ExecutionContext();
            ArrayList<String> nullList = new ArrayList<>();
            ec.putInt(KEY_SIZE, 0);
            String redisKey = cacheKey + ":" + IDWorker.getInstance().nextId();
            //设置超时时间为2天
            cacheOperation.put(redisKey, listToString(nullList), TIMEOUT_SECOND);
            ec.put(KEY_CONTEXT_KEY, redisKey);
            result.put(MessageFormat.format("part{0,number,000}", i), ec);
        }
        logger.error("实际网络数量[{}]", result.size());
        return result;
    }

    private String listToString(List<String> lists){
        StringBuilder sb = new StringBuilder("");
        for(String str : lists){
            sb.append(str);
            sb.append(",");
        }
        String refString = sb.toString();
        if(StringUtils.isEmpty(refString)){
            return "";
        }
        return refString.substring(0, refString.length() - 1);
    }

    private  List<String> stringToList(String str){
        if(StringUtils.isEmpty(str)){
            return new ArrayList<>();
        }
        String[] strs = str.split(",");
        List<String> lists = new ArrayList<>();
        for(String s : strs){
            lists.add(s);
        }
        return lists;
    }

    public StepExecution getStepExecution(){
        return this.masterStepException;
    }
    public static List<String> convert(List<Long> lists){
        List<String> res = new ArrayList<>();
        for(Long item : lists){
            res.add(String.valueOf(item));
        }
        return res;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution){
        //由于继承了AbstractItemCountingItemStreamItemReader,doOpen里拿不到ExecutionContext,所以只能在这里除了
        //这里的处理在事务之外
        ExecutionContext ec = stepExecution.getExecutionContext();
        if(ec.containsKey(KEY_CONTEXT_KEY)){
            //如果有Patitioner或断定续批
            String redisKey = ec.getString(KEY_CONTEXT_KEY);
            allkeys = stringToList(cacheOperation.get(redisKey));
        }else{
            //如果没有Patitioner,就在这里把所有的主键加载，病情写入ExecutionContext
            //但这个ExecutionContext会在第一次update时写入数据库，这个操作是在chunk的事务之外的
            allkeys = loadKeys();
            String redisKey = cacheKey + ":" + IDWorker.getInstance().nextId();
            //设置超时时间为2天
            cacheOperation.put(redisKey, listToString(allkeys), TIMEOUT_SECOND);
            ec.put(KEY_CONTEXT_KEY, redisKey);
            logger.info("加载[{}]条主键信息", allkeys.size());
        }
    }

    //set/get
    public int getMinPartitionSize() {
        return minPartitionSize;
    }

    public void setMinPartitionSize(int minPartitionSize) {
        this.minPartitionSize = minPartitionSize;
    }

    public int getMaxPartitionSize() {
        return maxPartitionSize;
    }

    public void setMaxPartitionSize(int maxPartitionSize) {
        this.maxPartitionSize = maxPartitionSize;
    }

    public StepExecution getMasterStepException() {
        return masterStepException;
    }

    public void setMasterStepException(StepExecution masterStepException) {
        this.masterStepException = masterStepException;
    }
}
