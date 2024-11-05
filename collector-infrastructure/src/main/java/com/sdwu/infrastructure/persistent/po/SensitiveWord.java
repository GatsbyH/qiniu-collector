package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author gatsbyh
 * @description 敏感词
 * @create 2024-01-07 08:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sensitive_word")
public class SensitiveWord {

    /** 自增ID */
    private Long id;
    /** 类型；allow-允许，deny-拒绝 */
    private String wordType;
    /** 敏感词 */
    private String word;
    /** 是否有效；0无效、1有效 */
    private Integer isValid;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
