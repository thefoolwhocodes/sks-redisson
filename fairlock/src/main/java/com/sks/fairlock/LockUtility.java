/**
 * Utility class that could get lock and release lock.
 * lockkey is formed using 
 */

package com.sks.fairlock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class LockUtility {
	private final RedissonClient _redisson;
	
	public LockUtility()
	{
		_redisson = RedisClientProvider.getRedissonClient();
	}
	
	public RLock getLock(String masterId, String lockId)
	{
		String lockKey = lockId + ":LOCK";
		System.out.println(String.format("    %s: LockUtility before getting lock %s ",masterId,lockKey));
		RLock fairLock = _redisson.getFairLock(lockKey);
		fairLock.lock();
		System.out.println(String.format("    %s: LockUtility after getting lock %s ",masterId,lockKey));
		return fairLock;
	}
	
	public void releaseLock(RLock fairLock)
	{
		fairLock.unlock();
	}
}
