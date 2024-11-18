package com.sdwu.trigger.http;

import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.domain.github.service.IFieldMappingService;
import com.sdwu.types.model.PageResult;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/fieldMapping")
public class FieldMappingController {

    @Resource
    private IFieldMappingService fieldMappingService;

    @GetMapping("/list")
    public Response list(FieldMapping fieldMapping) {
        PageResult<FieldMapping> pageResult = fieldMappingService.selectFieldMappingPage(fieldMapping);
        return Response.success(pageResult);
    }

    @GetMapping("/{mappingId}")
    public Response getInfo(@PathVariable Long mappingId) {
        return Response.success(fieldMappingService.getFieldMapping(mappingId));
    }

    @GetMapping("/byFieldName/{fieldName}")
    public Response getByFieldName(@PathVariable String fieldName) {
        return Response.success(fieldMappingService.getFieldMappingsByFieldName(fieldName));
    }

    @PostMapping
    public Response add(@RequestBody FieldMapping fieldMapping) {
        return Response.success(fieldMappingService.insertFieldMapping(fieldMapping));
    }

    @PutMapping
    public Response edit(@RequestBody FieldMapping fieldMapping) {
        return Response.success(fieldMappingService.updateFieldMapping(fieldMapping));
    }

    @DeleteMapping("/{mappingId}")
    public Response remove(@PathVariable Long mappingId) {
        return Response.success(fieldMappingService.deleteFieldMapping(mappingId));
    }

    @PostMapping("/batch")
    public Response batchSave(@RequestBody List<FieldMapping> mappings) {
        return Response.success(fieldMappingService.batchSaveFieldMappings(mappings));
    }
}
