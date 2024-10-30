package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScheduledTaskServiceImpl implements IScheduledTaskService{
    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;
    @Override
    public PageResult<ScheduledTask> selcetScheduledTaskPage(ScheduledTask task) {
        return scheduledTaskRepository.selcetScheduledTaskPage(task);
    }
}
