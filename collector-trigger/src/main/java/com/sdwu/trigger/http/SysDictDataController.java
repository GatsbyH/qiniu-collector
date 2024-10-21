package com.sdwu.trigger.http;

import com.sdwu.domain.sysuser.model.entity.SysDictData;
import com.sdwu.domain.sysuser.repository.ISysDictTypeRepository;
import com.sdwu.domain.sysuser.service.ISysDictTypeService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {
    @Resource
    private ISysDictTypeService dictTypeService;


    @GetMapping(value = "/type/{dictType}")
    public Response dataType(@PathVariable String dictType) {
        List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(dictType);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(sysDictData)
                .build();
    }

}
