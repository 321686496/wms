package com.hongan.template.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Slf4j
public class SnowFlakeGeneratorId {

    private final static int MAX_QUEUE = 128;
    private final static int DEFAULT_MACHINE_BIT_NUM = 6; //机器标识占用的位数
    private final static int DEFAULT_PORT_BIT_NUM = 4; //端口占用的位数
    private final static long START_STAMP = 1641286225129L; //起始时间

    // 线程安全队列
    private final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Long> orderQueue = new ConcurrentLinkedQueue<>();

    /**
     * 可分配的位数
     */
    private final static int REMAIN_BIT_NUM = 22;

    @Value("${server.port}")
    private String port;

    @PostConstruct
    public void init() {

        int portBitNum = DEFAULT_PORT_BIT_NUM;
        int sequenceBitNum = REMAIN_BIT_NUM - portBitNum - machineBitNum;

        this.maxSequenceValue = ~(-1 << sequenceBitNum);

        machineBitLeftOffset = sequenceBitNum;
        portBitLeftOffset = portBitNum + sequenceBitNum;
        timestampBitLeftOffset = portBitNum + machineBitNum + sequenceBitNum;

        this.portId = Integer.parseInt(port) & 15;
        this.machineId = getMachineId() & 63;
        productIdToQueue(queue, MAX_QUEUE);
        productIdToQueue(orderQueue, MAX_QUEUE);
    }


    public Long getId() {
        Long peek = queue.poll();
        if (queue.size() <= MAX_QUEUE / 2) {
            productIdToQueue(queue, MAX_QUEUE / 2);
        }
        return peek;
    }

    public Long getOrderId() {
        Long peek = orderQueue.poll();
        if (orderQueue.size() <= MAX_QUEUE / 2) {
            productIdToQueue(orderQueue, MAX_QUEUE / 2);
        }
        return peek;
    }

    /**
     * 产生下一个ID
     */
    public synchronized Long nextId() {
        long currentStamp = getTimeMill();
        if (currentStamp < lastStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastStamp - currentStamp));
        }

        //新的毫秒，序列从0开始，否则序列自增
        if (currentStamp == lastStamp) {
            sequence = (sequence + 1) & this.maxSequenceValue;
            if (sequence == 0L) {
                //Twitter源代码中的逻辑是循环，直到下一个毫秒
                lastStamp = tilNextMillis();
//                throw new IllegalStateException("sequence over flow");
            }
        } else {
            sequence = 0L;
        }

        lastStamp = currentStamp;

        return (currentStamp - START_STAMP) << timestampBitLeftOffset | portId << portBitLeftOffset | machineId << machineBitLeftOffset | sequence;
    }


    private void productIdToQueue(ConcurrentLinkedQueue<Long> queue, Integer num) {
        new Thread(() -> {
            for (int i = 0; i < num; i++) {
                queue.add(nextId());
            }
        }).start();
    }

    private Integer getMachineId() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        /*获取本机IP*/
        assert addr != null;
        String ip = addr.getHostAddress();
        log.info("本机IP ： {}  ", ip);

        return Integer.parseInt(ip.split("\\.")[3]);
    }

    private final int machineBitNum = DEFAULT_MACHINE_BIT_NUM;

    /**
     * idc编号
     */
    private long portId;

    /**
     * 机器编号
     */
    private long machineId;

    /**
     * 当前序列号
     */
    private long sequence = 0L;

    /**
     * 上次最新时间戳
     */
    private long lastStamp = -1L;

    /**
     * idc偏移量：一次计算出，避免重复计算
     */
    private int portBitLeftOffset;

    /**
     * 机器id偏移量：一次计算出，避免重复计算
     */
    private int machineBitLeftOffset;

    /**
     * 时间戳偏移量：一次计算出，避免重复计算
     */
    private int timestampBitLeftOffset;

    /**
     * 最大序列值：一次计算出，避免重复计算
     */
    private int maxSequenceValue;

    private long getTimeMill() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis() {
        long timestamp = getTimeMill();
        while (timestamp <= lastStamp) {
            timestamp = getTimeMill();
        }
        return timestamp;
    }
}