package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.GitHubClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GitHubController {
    private final GitHubClientService gitHubClientService;

    @Autowired
    public GitHubController(GitHubClientService gitHubClientService) {
        this.gitHubClientService = gitHubClientService;
    }

    @GetMapping("/user")
    public String getUser() throws IOException {
        return gitHubClientService.fetchGitHubApi("/users/gatsbyh", null);
    }
}
