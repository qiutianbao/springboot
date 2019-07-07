package com.jxtb.batch.common.utils.str;

import com.jxtb.batch.common.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 移植writer的snowfake
 * （a)id构成：42位的时间前缀  + 10位的借点标识 + 12位的sequence避免并发的数字(12位不够用时强制得到新的时间前缀)
 * 注意这里进行了小改动：snowkfake是5位的datacenter加5位的机器id：这里编程使用10位的机器id
 * （b）对系统时间的依赖性非常强，需关闭ntp的十几家调整后，将会借据分配id
 * Created by jxtb on 2019/7/1.
 */
public class IDWorker {

    protected static final Logger logger = LoggerFactory.getLogger(IDWorker.class);
    private static long workId;
    private static long dataId;
    private static final int DEFAULT_DATA = 31;
    private static final Random RANDOM = new Random();
    static {
        workId = SystemPropertyUtil.getLong("seq_workId",RANDOM.nextInt(DEFAULT_DATA));
        dataId = RANDOM.nextInt(DEFAULT_DATA);
    }

    private static IDWorker instance = new IDWorker(workId, dataId);
    private long workerId;
    private long datacenterId;
    // 0，并发控制
    private long sequence = 0L;
    //时间起始标识点，作为基准，一般取系统的最近时间
    private long twepoch = 1288834974657L;
    //机器标识位数
    private long workerIdBits = 5L;
    //数据中心标识位数
    private long datacenterIdBits = 5L;
    //机器ID最大值：1023
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    //数据中心ID最大值
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    //毫秒内自增位
    private long sequenceBits = 12L;
    private long workerIdShift = sequenceBits; //12
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long lastTimestamp = -1L;

    public IDWorker(long workId, long datacenterId){
        if(workId > maxWorkerId || workId < 0){
            throw new IllegalArgumentException(String.format("机器标位数不能大于 %d 或者小于 0", maxWorkerId));
        }
        if(datacenterId > maxDatacenterId || datacenterId < 0){
            throw new IllegalArgumentException(String.format("数据中心标位数不能大于 %d 或者小于 0", maxDatacenterId));
        }
        this.workerId = workId;
        this.datacenterId = datacenterId;
        logger.debug(String.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workId));
    }

    public static IDWorker getInstance() { return instance; }

    public synchronized long nextId(){
        long timestamp = timeGen();
        if(timestamp < lastTimestamp){
            logger.error(String.format("机器时钟错误，至 %d 不能产生ID", lastTimestamp));
            throw new RuntimeException(String.format("机器时钟错误，期间 %d 不能产生ID", lastTimestamp - timestamp));
        }
        //如果上一个timestamp与新生产的相等，则sequence加-- (0-4095循环);对新的timestamp,sequence 从0开始
        if(lastTimestamp == timestamp){
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0){
                //重新生成timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        }else{
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 获取系统当前毫秒数
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 等待下一个毫秒的到来，保证返回的毫秒数在参数lastTimestamp之后
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp){
            timestamp = timeGen();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        IDWorker idWorker = new IDWorker(1,1);
        System.out.println(idWorker.nextId());
        System.out.println(idWorker.nextId());
    }

}
