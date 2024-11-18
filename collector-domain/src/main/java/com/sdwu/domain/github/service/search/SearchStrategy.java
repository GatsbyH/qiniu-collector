package com.sdwu.domain.github.service.search;

/**
 * 搜索策略接口
 * 定义了不同搜索方式需要实现的方法
 */
public interface SearchStrategy {
    /**
     * 获取查询模板文件名
     */
    String getQueryTemplate();

    /**
     * 格式化搜索词
     */
    String formatSearchTerm(String term);

    /**
     * 获取搜索类型标识
     */
    String getSearchType();

    /**
     * 获取查询参数名
     */
    String getQueryParamName();
} 