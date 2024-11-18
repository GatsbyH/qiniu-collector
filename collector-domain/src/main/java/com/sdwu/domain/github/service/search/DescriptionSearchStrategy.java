package com.sdwu.domain.github.service.search;

import org.springframework.stereotype.Component;

@Component
public class DescriptionSearchStrategy implements SearchStrategy {
    @Override
    public String getQueryTemplate() {
        return "searchRepositoriesByDescriptionQuery.graphql";
    }

    @Override
    public String formatSearchTerm(String term) {
        return term;
    }

    @Override
    public String getSearchType() {
        return "DESCRIPTION";
    }

    @Override
    public String getQueryParamName() {
        return "$descrip";
    }
} 