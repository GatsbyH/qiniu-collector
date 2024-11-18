package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.infrastructure.persistent.po.ScheduledTaskPO;
import com.sdwu.infrastructure.persistent.po.SystemUserPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface IScheduledTaskDao extends BaseMapperX<ScheduledTaskPO> {

    default PageResult<ScheduledTaskPO> selcetScheduledTaskPage(ScheduledTask task){
        // 从params中获取beginTime和endTime，如果不存在则返回空字符串
        String beginTime = Objects.toString(task.getParams().get("beginTime"), null);
        String endTime = Objects.toString(task.getParams().get("endTime"), null);
        return selectPage(task,new LambdaQueryWrapperX<ScheduledTaskPO>()
                .eqIfPresent(ScheduledTaskPO::getField, task.getField())
                .eqIfPresent(ScheduledTaskPO::getStatus, task.getStatus())
                .eqIfPresent(ScheduledTaskPO::getLastFailureMessage, task.getLastFailureMessage())
                .eqIfPresent(ScheduledTaskPO::getLastExecutionTime, task.getLastExecutionTime())
                .eqIfPresent(ScheduledTaskPO::getNextExecutionTime, task.getNextExecutionTime())
                .eqIfPresent(ScheduledTaskPO::getId, task.getId())
                .betweenIfPresent(ScheduledTaskPO::getCreateTime, beginTime, endTime)
        );
    };

    default int updateByField(ScheduledTaskPO scheduledTaskPO){
        return update(scheduledTaskPO,new LambdaQueryWrapperX<ScheduledTaskPO>()
                .eq(ScheduledTaskPO::getField, scheduledTaskPO.getField()));
    };

    default List<ScheduledTaskPO> selcetScheduledTaskByField(String field){
        return selectList(new LambdaQueryWrapperX<ScheduledTaskPO>()
                .eq(ScheduledTaskPO::getField, field));
    };

    default List<ScheduledTaskPO> findAllByStatusIn(List<String> list){
        return selectList(new LambdaQueryWrapperX<ScheduledTaskPO>()
                .in(ScheduledTaskPO::getStatus, list));
    };

    default List<String> getDeveloperFields(){
        List<ScheduledTaskPO> scheduledTaskPOS = selectList(new LambdaQueryWrapperX<ScheduledTaskPO>()
                .eq(ScheduledTaskPO::getStatus, "COMPLETED"));
        return scheduledTaskPOS.stream()
                .map(ScheduledTaskPO::getField)
                .distinct()
                .collect(Collectors.toList());
    };

    default int insertTask(ScheduledTaskPO scheduledTaskPO) {
        return insert(scheduledTaskPO);
    }
}
