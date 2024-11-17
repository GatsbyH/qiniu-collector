package com.sdwu.types.common;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    public static final String GITHUB_USER_INFO_KEY = "github_user_info:";
    public static final String GITHUB_PAGE = "github_page:";
    public static final String FETCHING_KEY = "github_isFetching:";
    public static final String GITHUB_NX = "github_nx:";
    public static final String GITHUB_USER_LOGIN = "github_user_login:";
    public static final String GITHUB_EMPTY_COUNT = "github_empty_count:";

    public static final String USER_STATS_CACHE_KEY = "github:user:stats:";

    public static final long USER_STATS_CACHE_TIME = 86400000; // 1天

    public static final String LOCK_PREFIX = "github:fetch:";
    public static final String CHECK_TASKS_LOCK = "check-tasks-lock:";

}
