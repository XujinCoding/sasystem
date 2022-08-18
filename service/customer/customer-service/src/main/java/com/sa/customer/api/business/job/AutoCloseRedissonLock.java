package com.sa.customer.api.business.job;

import org.redisson.RedissonFairLock;
import org.redisson.command.CommandAsyncExecutor;

/**
 * @author xujin
 */
public class AutoCloseRedissonLock extends RedissonFairLock implements Cloneable  {
    
    public AutoCloseRedissonLock(CommandAsyncExecutor commandExecutor, String name) {
        super(commandExecutor, name);
    }

    public AutoCloseRedissonLock(CommandAsyncExecutor commandExecutor, String name, long threadWaitTime) {
        super(commandExecutor, name, threadWaitTime);
    }

    @Override
    public AutoCloseRedissonLock clone() {
        try {
            return (AutoCloseRedissonLock) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
