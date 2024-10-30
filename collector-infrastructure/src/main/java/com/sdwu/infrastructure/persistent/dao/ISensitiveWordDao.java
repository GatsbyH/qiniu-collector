package com.sdwu.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ISensitiveWordDao {

    List<String> queryValidSensitiveWordConfig(String wordType);

}
