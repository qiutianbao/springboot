package com.jxtb.sdk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by jxtb on 2019/6/19.
 */
public class IdWorker {
    protected static final Logger logger = LoggerFactory.getLogger(IdWorker.class);
    private static long workId;
    private static long dataId;
    private static final int DEFAULT_DATE = 31;
    private static final Random RANDOM = new Random();
    private static IdWorker instance;
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long twepoch = 1288834974657L;
    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    private long maxWorkerId;
    private long maxDatacenterId;
    private long sequenceBits;
    private long workerIdShit;
    private long datacenterIdShift;
    private long timestampLeftShift;
    private long sequenceMask;
    private long lastTimestamp;

    public IdWorker(long workerId, long datacenterId){
        this.maxWorkerId = ~(-1L << (int)this.workerIdBits);
        this.maxDatacenterId = ~(-1L << (int)this.datacenterIdBits);
        this.sequenceBits = 12L;
        this.workerIdShit = this.sequenceBits;
        this.datacenterIdShift = this.sequenceBits + this.workerIdBits;
        this.timestampLeftShift = this.sequenceBits + this.datacenterIdBits;
        this.sequenceMask = ~(-1L << (int)this.sequenceBits);
        this.lastTimestamp = -1L;
        if(workerId <= this.maxWorkerId && workerId > 0L){
            if(datacenterId <= this.maxDatacenterId && datacenterId >= 0L){
                this.workerId = workerId;
                this.datacenterId = datacenterId;
                logger.info("worker starting timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d,workerid %d",
                        this.timestampLeftShift, this.datacenterIdBits, this.workerIdBits, this.sequenceBits, workerId);
            }else{
                throw new IllegalArgumentException(String.format("数据中心标示位数不能大于 %d 或者小于0",this.maxDatacenterId));
            }
        }else{
            throw new IllegalArgumentException(String.format("机器标示位数不能大于 %d 或者小于0",this.maxWorkerId));
        }
    }

    public static IdWorker getInstance(){
        return instance;
    }

    public synchronized long nextId(){
        long timestamp = this.timeGen();
        if(timestamp < this.lastTimestamp){
            logger.error(String.format("机器时钟错误，至 %d 不能产生ID", this.lastTimestamp));
            throw new RuntimeException(String.format("机器时钟错误，期间 %d 不能产生ID", this.lastTimestamp - timestamp));
        }else{
            if(this.lastTimestamp == timestamp){
                this.sequence = this.sequence + 1L & this.sequenceMask;
                if(this.sequence == 0L){
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            }else{
                this.sequence = 0L;
            }
        }
        this.lastTimestamp = timestamp;
        return timestamp - this.twepoch << (int)this.timestampLeftShift | this.datacenterId << (int)this.datacenterIdShift | this.workerId << (int)this.workerIdShit | this.sequence;
    }

    private long tilNextMillis(long lastTimestamp){
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()){
            ;
        }
        return timestamp;
    }

    protected long timeGen(){
        return System.currentTimeMillis();
    }

    static {
        workId = SystemPropertyUtil.getLong("seq_workId", (long)RANDOM.nextInt(31));
        dataId = (long)RANDOM.nextInt(31);
        instance = new IdWorker(workId, dataId);
    }

}
