package com.sdwu.domain.github.model.entity;

import com.sdwu.types.model.PageParam;
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
public class ScheduledTask extends PageParam implements Serializable {
    private Integer id; // 主键ID，自动增长
    private String field; // 领域
    private String status; // 任务状态
    private LocalDateTime lastExecutionTime; // 上次执行时间
    private LocalDateTime nextExecutionTime; // 下次执行时间
    private String lastFailureMessage; // 上次失败信息
}
