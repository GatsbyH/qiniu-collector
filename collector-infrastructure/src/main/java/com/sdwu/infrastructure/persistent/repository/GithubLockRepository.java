package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.repository.IGithubLockRepository;
import com.sdwu.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.sdwu.types.common.CacheConstants.LOCK_PREFIX;

@Repository
@Slf4j
public class GithubLockRepository implements IGithubLockRepository {

    @Resource
    private IRedisService redisService;


    @Override
    public boolean tryLockByField(String field) {
        try {
            RLock lock = redisService.getLock(LOCK_PREFIX + field);
            return lock.tryLock( 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("无法获取lock: {}", field);
            return false;
        }
    }

    @Override
    public boolean removeFieldSearchLock(String field) {
        if (redisService.isExists(LOCK_PREFIX + field)) {
            redisService.remove(LOCK_PREFIX + field);
            return true;
        }
        return false;
    }

    @Override
    public boolean isLockedByCurrentThread(String field) {
        if (redisService.isExists(LOCK_PREFIX + field)) {
            return redisService.getLock(LOCK_PREFIX + field).isHeldByCurrentThread();
        }
        return false;
    }
}
