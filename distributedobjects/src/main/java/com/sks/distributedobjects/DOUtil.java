package com.sks.distributedobjects;

import com.sks.common.RedisClientProvider;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public final class DOUtil {

    private  static final RedissonClient R_CLIENT;

    static {
        R_CLIENT = RedisClientProvider.getRedissonClient();
    }

    public static void demonstrateRBucket()
    {
        {
            Employee emp1 = new Employee("Shashi", 35, "INDIA");
            RBucket<Employee> bucket = R_CLIENT.getBucket("Shashi");
            bucket.set(emp1);
        }

        {
            RBucket<Employee> bucket = R_CLIENT.getBucket("Shashi");
            Employee emp1 = bucket.get();
            emp1.Display();
        }

        {
            RBucket<Employee> bucket = R_CLIENT.getBucket("Shashi");
            bucket.delete();
        }
    }
}
