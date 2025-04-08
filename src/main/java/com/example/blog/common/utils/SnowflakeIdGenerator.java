package com.example.blog.common.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {
    private static final long START_TIMESTAMP = 1625097600000L; // 2021-07-01
    private static final long WORKER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator() {
        // 从配置中获取workerId（建议0-31之间）
        this.workerId = 1; // 示例值
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        long timestamp = System.currentTimeMillis();

        synchronized (this) {
            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards");
            }

            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            return ((timestamp - START_TIMESTAMP) << (WORKER_ID_BITS + SEQUENCE_BITS))
                    | (workerId << SEQUENCE_BITS)
                    | sequence;
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}

