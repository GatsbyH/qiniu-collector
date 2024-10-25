package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.GitHubClientService;
import com.sdwu.domain.github.service.ITalentRankService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
public class GitHubController {
    private final GitHubClientService gitHubClientService;

    @Resource
    private ITalentRankService talentRankService;

    @Autowired
    public GitHubController(GitHubClientService gitHubClientService) {
        this.gitHubClientService = gitHubClientService;
    }

    @GetMapping("/user")
    public String getUser() throws IOException {
        return gitHubClientService.fetchGitHubApi("/users/gatsbyh", null);
    }

    @GetMapping("getTalentRankByUserName")
    public Response getTalentRankByUserName(String username) throws IOException {
        double talentRankByUserName = talentRankService.getTalentRankByUserName(username);
        return Response.builder()
                .code(200)
                .info("success")
                .data(talentRankByUserName)
                .build();
    }
}
