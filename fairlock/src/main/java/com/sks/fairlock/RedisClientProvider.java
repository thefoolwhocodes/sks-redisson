package com.sks.fairlock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public final class RedisClientProvider 
{
    private static RedissonClient _client;

    private static class InstanceHolder {
        public static RedisClientProvider instance = new RedisClientProvider();
    }

    private RedisClientProvider() {
        try {
            Config config = new Config();
            if(Boolean.parseBoolean(Prop.getProp().getProperty("single_server_redis","true"))) {
                System.out.println("RedisClientProvider - Single server enabled using uri " + Prop.getProp().getProperty("redis_service_uri","redis://localhost:6379"));
                SingleServerConfig singleServerConfig = config.useSingleServer();
                singleServerConfig.setAddress(Prop.getProp().getProperty("redis_service_uri","redis://localhost:6379"));
                singleServerConfig.setSubscriptionConnectionPoolSize(Integer.parseInt(Prop.getProp().getProperty("redis_subscription_connection_pool_size","100")));
                singleServerConfig.setSubscriptionConnectionMinimumIdleSize(Integer.parseInt(Prop.getProp().getProperty("redis_subscription_idle_connection_pool_size","50")));
                singleServerConfig.setSubscriptionsPerConnection(Integer.parseInt(Prop.getProp().getProperty("redis_subscriptions_per_connection","5")));
                singleServerConfig.setRetryInterval(Integer.parseInt(Prop.getProp().getProperty("redis_retry_interval","3000")));
                singleServerConfig.setTimeout(Integer.parseInt(Prop.getProp().getProperty("redis_connection_timeout","10000")));
                singleServerConfig.setRetryAttempts(Integer.parseInt(Prop.getProp().getProperty("redis_num_retries","10")));
            }
            else {
                System.out.println("RedisClientProvider - Cluster server enabled using uri " + Prop.getProp().getProperty("redis_service_uri","redis://localhost:6377 "
                        + "redis://localhost:6378 redis://localhost:6379"));
                ClusterServersConfig clusterServersConfig = config.useClusterServers();
                clusterServersConfig.setScanInterval(Integer.parseInt(Prop.getProp().getProperty("redis_cluster_scan_interval","2000")));
                clusterServersConfig.addNodeAddress(Prop.getProp().getProperty("redis_service_uri","redis://localhost:6377"
                        + " redis://localhost:6378 redis://localhost:6379").split(" "));
                clusterServersConfig.setMasterConnectionPoolSize(Integer.parseInt(Prop.getProp().getProperty("redis_master_connection_pool_size","1")));
                clusterServersConfig.setMasterConnectionMinimumIdleSize(Integer.parseInt(Prop.getProp().getProperty("redis_master_idle_connection_pool_size","1")));
                clusterServersConfig.setSubscriptionConnectionPoolSize(Integer.parseInt(Prop.getProp().getProperty("redis_subscription_connection_pool_size","100")));
                clusterServersConfig.setSubscriptionConnectionMinimumIdleSize(Integer.parseInt(Prop.getProp().getProperty("redis_subscription_idle_connection_pool_size","50")));
                clusterServersConfig.setSubscriptionsPerConnection(Integer.parseInt(Prop.getProp().getProperty("redis_subscriptions_per_connection","5")));
                clusterServersConfig.setRetryInterval(Integer.parseInt(Prop.getProp().getProperty("redis_retry_interval","3000")));
                clusterServersConfig.setTimeout(Integer.parseInt(Prop.getProp().getProperty("redis_connection_timeout","10000")));
                clusterServersConfig.setRetryAttempts(Integer.parseInt(Prop.getProp().getProperty("redis_num_retries","10")));
            }
            _client = Redisson.create(config);
        } catch (Exception ex) {
            System.out.println("RedisClientProvider - Please check the connection between client and server" + ex);
            System.exit(1);
        }
    }

    public static RedisClientProvider getInstance() {
        return  InstanceHolder.instance;
    }

    public static RedissonClient getRedissonClient() {
        return _client;
    }
}
