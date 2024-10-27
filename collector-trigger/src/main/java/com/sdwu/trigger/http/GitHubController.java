package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.GitHubClientService;
import com.sdwu.domain.github.service.ITalentRankService;
import com.sdwu.domain.github.service.IDeveloperNationService;
import com.sdwu.types.annotation.Loggable;
import com.sdwu.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class GitHubController {
    private final GitHubClientService gitHubClientService;

    @Resource
    private ITalentRankService talentRankService;

    @Resource
    private IDeveloperNationService developerNationService;

    @Autowired
    public GitHubController(GitHubClientService gitHubClientService) {
        this.gitHubClientService = gitHubClientService;
    }

    @GetMapping("/user")
    public String getUser() throws IOException {
        return gitHubClientService.fetchGitHubApi("/users/gatsbyh", null);
    }

    @GetMapping("getTalentRankByUserName")
    @Loggable
    public Response getTalentRankByUserName(String username) throws IOException, ExecutionException, InterruptedException {
        double talentRankByUserName = talentRankService.getTalentRankByUserName(username);
        return Response.builder()
                .code(200)
                .info("success")
                .data(talentRankByUserName)
                .build();
    }

    //开发者的 Nation
    @GetMapping("getDeveloperNation")
    public Response getDeveloperNation(String username) throws IOException, ExecutionException, InterruptedException {
        return Response.builder()
                .code(200)
                .info("success")
                .data(developerNationService.getDeveloperNation(username))
                .build();
    }
}
