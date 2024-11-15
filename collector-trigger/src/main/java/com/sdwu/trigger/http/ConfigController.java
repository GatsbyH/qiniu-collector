package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.GitHubClientService;
import com.sdwu.types.annotation.DCCValue;
import lombok.extern.slf4j.Slf4j;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.api.transaction.TransactionOp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
public class ConfigController {

    @DCCValue("downgradeSwitch")
    private String downgradeSwitch;

    @DCCValue("userWhiteList")
    private String userWhiteList;


    @DCCValue("githubToken")
    private String githubToken;

    @Resource
    private GitHubClientService gitHubClientService;
    @Resource
    private CuratorFramework curatorFramework;

    /**
     * curl http://localhost:8080/getConfig/downgradeSwitch
     */
    @RequestMapping("/getConfig/downgradeSwitch")
    public String getConfigDowngradeSwitch() {
        return downgradeSwitch;
    }

    /**
     * curl http://localhost:8080/getConfig/userWhiteList
     */
    @RequestMapping("/getConfig/userWhiteList")
    public String getConfigUserWhiteList() {

        return userWhiteList;
    }
    /**
     * curl http://localhost:8080/getConfig/getGitHubToken
     */
    @RequestMapping("/getConfig/getGitHubToken")
    public String getGitHubToken() throws Exception {
        byte[] tokenBytes = curatorFramework.getData().forPath("/qiniu-collector/config/githubToken");
        String githubToken = new String(tokenBytes, StandardCharsets.UTF_8);
        log.info("获取GitHubToken: {}", githubToken);
        return githubToken;
    }

    /**
     * curl -X GET "http://localhost:8080/setConfig?downgradeSwitch=true&userWhiteList=xfg,xfg2"
     */
    @GetMapping("/setConfig")
    public void setConfig(Boolean downgradeSwitch, String userWhiteList) throws Exception {
        curatorFramework.setData().forPath("/qiniu-collector/config/downgradeSwitch", (downgradeSwitch ? "开" : "关").getBytes(StandardCharsets.UTF_8));
        curatorFramework.setData().forPath("/qiniu-collector/config/userWhiteList", userWhiteList.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * curl -X GET "http://localhost:8080/setGitHubToken?githubToken=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"
     */
    @GetMapping("/setGitHubToken")
    public void setGitHubToken(String githubToken) throws Exception {
        curatorFramework.setData().forPath("/qiniu-collector/config/githubToken", githubToken.getBytes(StandardCharsets.UTF_8));
        gitHubClientService.updateTokenFromZookeeper();
    }
    /**
     * 事务操作
     * curl -X GET "http://localhost:8080/setConfigExecuteTranSanction?downgradeSwitch=false&userWhiteList=xfg,user2,user3"
     */
    @GetMapping("/setConfigExecuteTranSanction")
    public List<CuratorTransactionResult> setConfigExecuteTranSanction(Boolean downgradeSwitch, String userWhiteList) throws Exception {
        TransactionOp transactionOp = curatorFramework.transactionOp();
        CuratorOp downgradeSwitchCuratorOp = transactionOp.setData().forPath("/qiniu-collector/config/downgradeSwitch", (downgradeSwitch ? "开" : "关").getBytes(StandardCharsets.UTF_8));
        CuratorOp userWhiteListCuratorOp = transactionOp.setData().forPath("/qiniu-collector/config/userWhiteList", userWhiteList.getBytes(StandardCharsets.UTF_8));
        List<CuratorTransactionResult> transactionResults = curatorFramework.transaction().forOperations(downgradeSwitchCuratorOp, userWhiteListCuratorOp);
        for (CuratorTransactionResult transactionResult : transactionResults) {
            log.info("事务结果：{} - {}", transactionResult.getForPath(), transactionResult.getType());
        }
        return transactionResults;
    }



}
