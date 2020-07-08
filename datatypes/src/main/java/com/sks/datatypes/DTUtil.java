package com.sks.datatypes;

import com.sks.common.RedisClientProvider;
import org.redisson.api.RedissonClient;

final class DTUtil {

    public static void demonstrateRedisStreams() {
        RedissonClient rClient = RedisClientProvider.getRedissonClient();
    }
}
