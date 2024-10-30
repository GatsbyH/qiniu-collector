package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("scheduled_tasks")
public class ScheduledTaskPO extends BaseDO implements Serializable {
    private Integer id; // 主键ID，自动增长
    private String field; // 领域
    private String status; // 任务状态
    private LocalDateTime lastExecutionTime; // 上次执行时间
    private LocalDateTime nextExecutionTime; // 下次执行时间
    private String lastFailureMessage; // 上次失败信息

    public static ScheduledTask convertToDomain(ScheduledTaskPO scheduledTaskPO) {
        return ScheduledTask.builder()
                .id(scheduledTaskPO.getId())
                .field(scheduledTaskPO.getField())
                .status(scheduledTaskPO.getStatus())
                .lastExecutionTime(scheduledTaskPO.getLastExecutionTime())
                .nextExecutionTime(scheduledTaskPO.getNextExecutionTime())
                .lastFailureMessage(scheduledTaskPO.getLastFailureMessage())
                .build();
    }
}
