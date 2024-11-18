package com.sdwu.domain.github.service.search;

import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 搜索策略上下文
 * 管理和提供不同的搜索策略
 */
@Component
public class SearchContext {
    private final Map<String, SearchStrategy> strategies = new ConcurrentHashMap<>();
    
    @Resource
    public void setStrategies(List<SearchStrategy> searchStrategies) {
        searchStrategies.forEach(strategy -> 
            strategies.put(strategy.getSearchType(), strategy));
    }
    
    public SearchStrategy getStrategy(String type) {
        SearchStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown search type: " + type);
        }
        return strategy;
    }
} 