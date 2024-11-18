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
    @Override
    public boolean insertTask(ScheduledTask task) {
        // 检查领域是否已存在
        if (scheduledTaskRepository.checkScheduledTaskByField(task.getField())) {
            throw new RuntimeException("该领域已存在");
        }
        return scheduledTaskRepository.insertScheduledTask(task.getField(), task.getStatus());
    }
}
