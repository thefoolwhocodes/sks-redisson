package com.sks.datatypes;

import com.sks.common.RedisClientProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RFuture;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.client.codec.StringCodec;

final class RedisStreamsUtil {
    private final static RedissonClient CLIENT = RedisClientProvider.getRedissonClient();
    private final static String GROUP = "GROUP_1";
    private final static String CONSUMER = "CONSUMER_1";
    private final static int READ_SIZE = 10;
    private final static int BLOCK_SECS = 1;

    private final static int STREAMS_COUNT = 10;
    private final static String STREAMS_PREFIX = "STREAM_";
    private static List<RStream<String, String>> streams;

    private final static String KEY_1 = "KEY_1";

    private static void initStream() {
        System.out.println("RedisStreamsUtil - initStream - Started");
        streams = new ArrayList<RStream<String, String>>(STREAMS_COUNT);

        for (int i = 0; i < STREAMS_COUNT; i++) {
            String streamIdentifier = STREAMS_PREFIX + i;
            RStream<String, String> stream = CLIENT.getStream(streamIdentifier, StringCodec.INSTANCE);
            streams.add(stream);

            if (!streams.get(i).isExists()) {
                System.out.println("initStream - creating GROUP and empty stream for key(stream):" + streamIdentifier);
                streams.get(i).createGroup(GROUP, StreamMessageId.ALL);
            }
        }
        System.out.println("RedisStreamsUtil - initStream - Finished");
    }

    private static void pushDataSync() {
        System.out.println("RedisStreamsUtil - pushDataSync - Started");
        for (int i = 0; i < STREAMS_COUNT; i++) {
            String streamIdentifier = STREAMS_PREFIX + i;
            StreamMessageId id = CLIENT.getStream(streamIdentifier).add(KEY_1, streamIdentifier);
            System.out.println("Message id:" + id.toString() + " Value:" + streamIdentifier);
        }
        System.out.println("RedisStreamsUtil - pushDataSync - Finished");
    }

    private static void pushDataAsync() {
        System.out.println("RedisStreamsUtil - pushDataAsync - Started");
        for (int i = 0; i < STREAMS_COUNT; i++) {
            String streamIdentifier = STREAMS_PREFIX + i;

            RFuture<StreamMessageId> future = CLIENT.getStream(streamIdentifier).addAsync(KEY_1,
                    streamIdentifier + "_async");
            future.onComplete((streamId, e) -> {
                if (e == null) {
                    System.out.println("RedisStreamsUtil - pushDataAsync - data pushed");
                    System.out.println("RedisStreamsUtil - message id:" + streamId.toString());
                } else {
                    System.out.println("RedisStreamsUtil - error :" + e);
                }
            });
        }
        System.out.println("RedisStreamsUtil - pushDataAsync - Finished");
    }

    private static void readData() {
        System.out.println("RedisStreamsUtil - readData - Started");
        for (int i = 0; i < STREAMS_COUNT; i++) {
            RStream<String, String> stream = streams.get(i);
            Map<StreamMessageId, Map<String, String>> logs = stream.readGroup(GROUP, CONSUMER, READ_SIZE, BLOCK_SECS,
                    TimeUnit.SECONDS, StreamMessageId.NEVER_DELIVERED);

            if (logs == null || logs.isEmpty()) {
                System.out.println("RedisStreamsUtil - readData - No data found in logs");
            } else {
                for (Map.Entry<StreamMessageId, Map<String, String>> m : logs.entrySet()) {
                    Map<String, String> fields = (Map<String, String>) m.getValue();

                    for (Map.Entry<String, String> m1 : fields.entrySet()) {
                        System.out.println(
                                "Message id:" + m.getKey() + " Key:" + m1.getKey() + " Value:" + m1.getValue());
                        stream.ack(GROUP, (StreamMessageId) m.getKey());
                    }
                }
            }
        }
        System.out.println("RedisStreamsUtil - readData - Finished");
    }

    public static void demonstrateRedisStreams() {
        System.out.println("RedisStreamsUtil - demonstrateRedisStreams - Started");
        initStream();
        pushDataSync();
        pushDataAsync();
        readData();
        System.out.println("RedisStreamsUtil - demonstrateRedisStreams - Finished");
    }
}
