package com.sdwu.infrastructure.persistent.repository;

import cn.hutool.core.date.DateUtil;
import com.sdwu.domain.github.event.FieldSearchMessageEvent;
import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.infrastructure.event.EventPublisher;
import com.sdwu.infrastructure.persistent.dao.IScheduledTaskDao;
import com.sdwu.infrastructure.persistent.po.ScheduledTaskPO;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ScheduledTaskRepository implements IScheduledTaskRepository {

    @Resource
    private IScheduledTaskDao scheduledTaskDao;
    @Resource
    private EventPublisher eventPublisher;


    @Resource
    private FieldSearchMessageEvent fieldSearchMessageEvent;
    @Override
    public PageResult<ScheduledTask> selcetScheduledTaskPage(ScheduledTask task) {
        PageResult<ScheduledTaskPO> scheduledTaskPOPageResult = scheduledTaskDao.selcetScheduledTaskPage(task);
        if (scheduledTaskPOPageResult==null){
            return PageResult.empty();
        }
        List<ScheduledTask> scheduledTasks = scheduledTaskPOPageResult.getList().stream()
                .map(ScheduledTaskPO::convertToDomain)
                .collect(Collectors.toList());
        return new PageResult<>(scheduledTasks, scheduledTaskPOPageResult.getTotal());
    }

    @Override
    public Boolean insertScheduledTask(String field,String status) {
        FieldSearchMessageEvent.FieldSearchMessage fieldSearchMessage = new FieldSearchMessageEvent.FieldSearchMessage();
        fieldSearchMessage.setField(field);
        eventPublisher.publish(fieldSearchMessageEvent.topic(),fieldSearchMessageEvent.buildEventMessage(fieldSearchMessage));
        ScheduledTaskPO scheduledTaskPO = ScheduledTaskPO.builder()
                .field(field)
                .status(status)
                .nextExecutionTime(null)
                .lastFailureMessage(null)
                .lastExecutionTime(LocalDateTime.now())
                .build();
        int insert = scheduledTaskDao.insert(scheduledTaskPO);
        if (insert>=1){
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateScheduledTask(String field, String status,String errorMessage) {
        ScheduledTaskPO scheduledTaskPO = ScheduledTaskPO.builder()
                .field(field)
                .status(status)
                .nextExecutionTime(null)
                .lastFailureMessage(errorMessage)
                .lastExecutionTime(LocalDateTime.now())
                .build();
        int update = scheduledTaskDao.updateByField(scheduledTaskPO);
        if (update>=1){
            return true;
        }
        return false;
    }

    @Override
    public Boolean endScheduledTask(String field, String status) {
        ScheduledTaskPO scheduledTaskPO = ScheduledTaskPO.builder()
                .field(field)
                .status(status)
                .nextExecutionTime(null)
                .lastFailureMessage(null)
                .lastExecutionTime(LocalDateTime.now())
                .build();
        int update = scheduledTaskDao.updateByField(scheduledTaskPO);
        if (update>=1){
            return true;
        }
        return false        ;
    }

    @Override
    public boolean checkScheduledTaskByField(String field) {
        List<ScheduledTaskPO> scheduledTaskPOS = scheduledTaskDao.selcetScheduledTaskByField(field);
        if (scheduledTaskPOS.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateScheduledTaskRUNNING(String field, String running) {
        ScheduledTaskPO scheduledTaskPO = ScheduledTaskPO.builder()
                .field(field)
                .status(running)
                .nextExecutionTime(null)
                .lastFailureMessage(null)
                .lastExecutionTime(LocalDateTime.now())
                .build();
        int update = scheduledTaskDao.updateByField(scheduledTaskPO);
        if (update>=1){
            return true;
        }
        return false;
    }

    @Override
    public List<ScheduledTask> findAllByStatusIn(List<String> list) {
        List<ScheduledTaskPO> scheduledTaskPOS = scheduledTaskDao.findAllByStatusIn(list);

            return scheduledTaskPOS.stream()
                    .map(ScheduledTaskPO::convertToDomain)
                    .collect(Collectors.toList());

    }
}
