package com.sdwu.trigger.http;

import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.domain.github.service.IScheduledTaskService;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.types.model.PageResult;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ScheduledTask")
public class ScheduledTaskController {


    @Resource
    private IScheduledTaskService scheduledTaskService;
    @GetMapping("/list")
    public Response list(ScheduledTask task) {
        PageResult<ScheduledTask> scheduledTaskPage = scheduledTaskService.selcetScheduledTaskPage(task);
        return Response.success(scheduledTaskPage);
    }

}
