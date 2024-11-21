package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.types.model.PageResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDeveloperFieldService {
    List<Developer> getDeveloperByFieldAndNation(String field, String nation) throws IOException;

    Boolean startGetDeveloperByField(String field);

    Boolean stopGetDeveloperByField(String field);

    PageResult<Developer> getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo);


    List<String> getDeveloperFields();

    List<Map<String, String>> getDeveloperNationOptionsByField(String field);
}
