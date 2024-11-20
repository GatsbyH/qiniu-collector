package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepoAnalysisData {
    private String name;
    private String description;
    private String language;
    private List<String> topics;
    private boolean isFork;
}
