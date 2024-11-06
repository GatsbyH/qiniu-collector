package com.sdwu.domain.github.repository;

import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.types.model.PageResult;

import java.util.List;

public interface IScheduledTaskRepository {
    PageResult<ScheduledTask> selcetScheduledTaskPage(ScheduledTask task);

    Boolean insertScheduledTask(String field,String status);

    Boolean updateScheduledTask(String field, String status,String errorMessage);

    Boolean endScheduledTask(String field, String status);

    boolean checkScheduledTaskByField(String field);

    boolean updateScheduledTaskRUNNING(String field, String running);

    List<ScheduledTask> findAllByStatusIn(List<String> list);

    List<String> getDeveloperFields();
}
