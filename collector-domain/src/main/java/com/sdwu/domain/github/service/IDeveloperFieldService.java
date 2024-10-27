package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.Developer;

import java.io.IOException;
import java.util.List;

public interface IDeveloperFieldService {
    List<Developer> getDeveloperByFieldAndNation(String field, String nation) throws IOException;
}
