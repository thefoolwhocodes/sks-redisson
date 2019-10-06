/**
 * Starting point of the application.
 * Loads work pool with a configured count.
 * Spawns threads of two types, work incrementors or work decrementors. 
 * Threads run indefinitely either increasing or decreasing values.
 */
package com.sks.fairlock;

import java.util.Random;

import org.redisson.api.RLock;

public class FairLock implements Runnable {
    private final boolean _increment;
    private final LockUtility _lockInterface;
    private final WorkHandler _workhandler;
    private final Random _randomWork;
    private final int _workPoolSize;

    public FairLock(boolean increment, WorkHandler resourceManipulator) {
        _increment = increment;
        _workhandler = resourceManipulator;
        _lockInterface = new LockUtility();
        _randomWork = new Random();
        _workPoolSize = Integer.parseInt(Prop.getProp().getProperty("work_pool_size", "10"));
    }

    // @Override
    public void run() {
        while (true) {
            String myName = Thread.currentThread().getName();
            System.out.println(String.format("%s:FairLock -.run starts", myName));

            String workKey = Integer.toString(_randomWork.nextInt(_workPoolSize) + 1);
            RLock rLock = _lockInterface.getLock(myName, workKey);

            Work resource = _workhandler.getResource(workKey);
            _workhandler.doWork(myName);
            if (true == _increment) {
                resource.incrementResource();
            } else {
                resource.decrementResource();
            }
            resource.displayState(myName);
            _workhandler.updateResource(workKey, resource);

            _lockInterface.releaseLock(rLock);
            System.out.println(String.format("%s:FairLock.- run ends", myName));
        }
    }

    public static void main(String[] args) {
        Prop prop = Prop.getInstance();
        RedisClientProvider clientProvider = RedisClientProvider.getInstance();

        WorkHandler workHandler = new WorkHandler();
        workHandler.loadResources();

        boolean increment = false;

        int threadCount = Integer.parseInt(Prop.getProp().getProperty("thread_count", "40"));
        int milliSleep = Integer.parseInt(Prop.getProp().getProperty("sleep_before_next_spawn_in_millisec", "100"));
        for (int i = 0; i < threadCount; i++) {
            if (0 == i % 2) {
                increment = true;
            } else {
                increment = false;
            }

            FairLock fairlock = new FairLock(increment, workHandler);
            Thread t1 = new Thread(fairlock);
            t1.start();
            try {
                // sleep for milliseconds before spawning a new thread
                Thread.sleep(milliSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
