package com.sdwu.domain.github.service.search;

import org.springframework.stereotype.Component;

@Component
public class TopicSearchStrategy implements SearchStrategy {
    @Override
    public String getQueryTemplate() {
        return "searchRepositoriesByTopicAndDescriptionQuery.graphql";
    }

    @Override
    public String formatSearchTerm(String term) {
        return term.replaceAll("\\s+", "");
    }

    @Override
    public String getSearchType() {
        return "TOPIC";
    }

    @Override
    public String getQueryParamName() {
        return "$topic";
    }
} 