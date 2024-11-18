package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.types.model.PageResult;

public interface IScheduledTaskService {
    PageResult<ScheduledTask> selcetScheduledTaskPage(ScheduledTask task);
    boolean insertTask(ScheduledTask task);
}
