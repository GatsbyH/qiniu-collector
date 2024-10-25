package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_detail")
public class FileDetail extends BaseDO implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private String id; // 文件id

    private String url; // 文件访问地址
    private Long size; // 文件大小，单位字节
    private String filename; // 文件名称
    private String originalFilename; // 原始文件名
    private String basePath; // 基础存储路径
    private String path; // 存储路径
    private String ext; // 文件扩展名
    private String contentType; // MIME类型
    private String platform; // 存储平台
    private String thUrl; // 缩略图访问路径
    private String thFilename; // 缩略图名称
    private Long thSize; // 缩略图大小，单位字节
    private String thContentType; // 缩略图MIME类型
    private String objectId; // 文件所属对象id
    private String objectType; // 文件所属对象类型，例如用户头像，评价图片
    private String metadata; // 文件元数据
    private String userMetadata; // 文件用户元数据
    private String thMetadata; // 缩略图元数据
    private String thUserMetadata; // 缩略图用户元数据
    private String attr; // 附加属性
    private String fileAcl; // 文件ACL
    private String thFileAcl; // 缩略图文件ACL
    private String hashInfo; // 哈希信息
    private String uploadId; // 上传ID，仅在手动分片上传时使用
    private Integer uploadStatus; // 上传状态，仅在手动分片上传时使用


}
