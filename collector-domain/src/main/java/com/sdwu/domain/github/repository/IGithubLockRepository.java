package com.sdwu.domain.github.repository;

public interface IGithubLockRepository {

    boolean tryLockByField(String field);

    boolean removeFieldSearchLock(String field);

    boolean isLockedByCurrentThread(String field);

    boolean checkScheduledTasksLock();

    boolean removeScheduledTasksLock();

    boolean isLockedByCurrentThreadByTasks();
}
