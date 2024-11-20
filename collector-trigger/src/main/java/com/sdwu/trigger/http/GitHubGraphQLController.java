package com.sdwu.trigger.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.service.ITalentRankGraphQLService;
import com.sdwu.domain.github.service.renderer.StatsSvgRenderer;
import com.sdwu.types.annotation.Loggable;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/GraphQL/")
public class GitHubGraphQLController {
    @Resource
    private ITalentRankGraphQLService talentRankGraphQLService;
    @Resource
    private IGithubUserRepository githubUserRepository;
    @Resource
    private StatsSvgRenderer statsRenderer;
    //GraphQL获得talentRank
    @GetMapping("getTalentRankByUserName")
    @Loggable
    public Response getTalentRankByUserName(String username){
        DevelopeVo talentRank = talentRankGraphQLService.getDeveloperStatsByUserName(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(talentRank)
                .build();
    }


    //根据账号搜索用户使用的语言
    @GetMapping("getDeveloperLanguage")
    @Loggable
    public Response getDeveloperLanguage(String username)  {
        List<LanguageCountRespVo> developerLanguage = talentRankGraphQLService.getDeveloperLanguage(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerLanguage)
                .build();
    }


    //根据账号搜索用户的领域
    @GetMapping("getDeveloperFiled")
    @Loggable
    public Response getDeveloperFiled(String username)  {
        String developerFiled = talentRankGraphQLService.getDeveloperFiled(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFiled)
                .build();
    }


    @GetMapping("/stats/{username}")
    @Loggable
    public String getDeveloperStatsSvg(
            @PathVariable String username,
            @RequestParam(defaultValue = "default") String theme) {
        try {
               // 首先尝试从缓存获取
            String cachedSvg = githubUserRepository.getSvgCache(username, theme);
            if (cachedSvg != null) {
                log.debug("命中缓存，返回用户 {} 的SVG统计图", username);
                return cachedSvg;
            }

            // 缓存未命中，重新生成SVG
            log.debug("未命中缓存，开始生成用户 {} 的SVG统计图", username);

            DevelopeVo stats = talentRankGraphQLService.getDeveloperStatsByUserName(username);
            if (stats == null) {
                return renderErrorCard("未找到开发者信息");
            }

            List<LanguageCountRespVo> languageStats = talentRankGraphQLService.getDeveloperLanguage(username);
            String svg = statsRenderer.renderDeveloperStats(stats, languageStats);
            // 保存到缓存
            githubUserRepository.saveSvgCache(username, theme, svg);
            return svg;
        } catch (Exception e) {
            log.error("生成用户 {} 的统计信息时发生错误: {}", username, e.getMessage());
            return renderErrorCard("生成统计信息失败");
        }
    }

    private String renderErrorCard(String message) {
        return String.format("<svg width=\"495\" height=\"120\" viewBox=\"0 0 495 120\" xmlns=\"http://www.w3.org/2000/svg\">" +
                "<style>" +
                "  .error { font: 600 16px 'Segoe UI', Ubuntu, Sans-Serif; fill: #dc3545; }" +
                "  .error-bg { fill: #fffefe; stroke: #dc3545; }" +
                "</style>" +
                "<rect x=\"0.5\" y=\"0.5\" width=\"494\" height=\"119\" rx=\"4.5\" class=\"error-bg\"/>" +
                "<text x=\"25\" y=\"45\" class=\"error\">%s</text>" +
                "<text x=\"25\" y=\"85\" class=\"error\" style=\"font-size: 12px;\">请检查用户名后重试</text>" +
                "</svg>",
                message);
    }

    //根据账号评估开发者页面
    @GetMapping("getDeveloperAssessment")
    public Response getDeveloperAssessment(String username) throws IOException {
        DevelopeVo developerAssessment = talentRankGraphQLService.getDeveloperAssessment(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerAssessment)
                .build();
    }
    //测试领域搜索
    @GetMapping("testSearch")
    public Response testSearch(String topic) throws IOException {
        talentRankGraphQLService.fetchUserByRepoTopic(topic);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(null)
                .build();
    }
    //测试GraphQL
    @GetMapping("testGraphQL")
    public Response testGraphQL() {
        String testGraphQL = talentRankGraphQLService.testGraphQL();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(testGraphQL)
                .build();
    }
}
