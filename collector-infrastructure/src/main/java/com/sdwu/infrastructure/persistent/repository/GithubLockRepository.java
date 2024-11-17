package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.repository.IGithubLockRepository;
import com.sdwu.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.sdwu.types.common.CacheConstants.CHECK_TASKS_LOCK;
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
//        if (redisService.isExists(LOCK_PREFIX + field)) {
//            redisService.remove(LOCK_PREFIX + field);
//            return true;
//        }
        try {
            RLock lock = redisService.getLock(LOCK_PREFIX + field);
            lock.unlock();
            return true;
        } catch (Exception e) {
            log.error("无法释放lock: {}", field);
            return false;
        }
    }

    @Override
    public boolean isLockedByCurrentThread(String field) {
        if (redisService.isExists(LOCK_PREFIX + field)) {
            return redisService.getLock(LOCK_PREFIX + field).isHeldByCurrentThread();
        }
        return false;
    }

    @Override
    public boolean checkScheduledTasksLock() {
        try {
            RLock lock = redisService.getLock(CHECK_TASKS_LOCK);
            return lock.tryLock( 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("无法获取checkScheduledTasksLock");
            return false;
        }
    }

    @Override
    public boolean removeScheduledTasksLock() {
//        if (redisService.isExists(CHECK_TASKS_LOCK)){
//            redisService.remove(CHECK_TASKS_LOCK);
//            return true;
//        }
//        return false;
        try {
            RLock lock = redisService.getLock(CHECK_TASKS_LOCK);
            lock.unlock();
            return true;
        } catch (Exception e) {
            log.error("无法释放lock");
            return false;
        }
    }

    @Override
    public boolean isLockedByCurrentThreadByTasks() {
        if (redisService.isExists(CHECK_TASKS_LOCK)) {
            return redisService.getLock(CHECK_TASKS_LOCK).isHeldByCurrentThread();
        }
        return false;
    }
}
