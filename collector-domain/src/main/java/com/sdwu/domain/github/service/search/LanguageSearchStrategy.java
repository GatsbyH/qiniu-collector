package com.sdwu.domain.github.service.search;

import org.springframework.stereotype.Component;

@Component
public class LanguageSearchStrategy implements SearchStrategy {
    @Override
    public String getQueryTemplate() {
        return "searchRepositoriesByLanguageQuery.graphql";
    }

    @Override
    public String formatSearchTerm(String term) {
        return term;
    }

    @Override
    public String getSearchType() {
        return "LANGUAGE";
    }

    @Override
    public String getQueryParamName() {
        return "$language";
    }
} 