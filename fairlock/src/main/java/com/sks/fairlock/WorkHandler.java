/**
 * Loads individual work in Redis.
 * Provides a doWork method to mimic some work with Random time taken to complete the work.
 */
package com.sks.fairlock;

import java.util.Random;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class WorkHandler {
    private final RedissonClient _redisson;
    private final Random _randomWorkDelay;
    private final int _upperBound;
    private final int _lowerBound;
    private final int _workPoolSize;

    WorkHandler() {
        _redisson = RedisClientProvider.getRedissonClient();
        _randomWorkDelay = new Random();
        _upperBound = Integer.parseInt(Prop.getProp().getProperty("work_upper_bound_delay", "1000"));
        _lowerBound = Integer.parseInt(Prop.getProp().getProperty("work_lower_bound_delay", "100"));
        _workPoolSize = Integer.parseInt(Prop.getProp().getProperty("work_pool_size", "10"));
    }

    public void updateResource(String key, Work resource) {
        RBucket<Work> bucket = _redisson.getBucket(key);
        bucket.set(resource);
    }

    public Work getResource(String key) {
        RBucket<Work> bucket = _redisson.getBucket(key);
        Work resource = bucket.get();
        return resource;
    }

    public void doWork(String masterId) {
        int randomNumber = _randomWorkDelay.nextInt(_upperBound - _lowerBound) + _lowerBound;
        try {
            System.out.println(String.format("    %s: Thread sleep for %fs", masterId, randomNumber / (float) 1000));
            Thread.sleep(randomNumber);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void loadResources() {
        for (int i = 0; i < _workPoolSize; i++) {
            Work resource = new Work(i + 1);
            updateResource(Integer.toString(resource.getId()), resource);
        }
    }
}
