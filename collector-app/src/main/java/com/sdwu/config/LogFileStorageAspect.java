package com.sdwu.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.UploadPretreatment;
import org.dromara.x.file.storage.core.aspect.DeleteAspectChain;
import org.dromara.x.file.storage.core.aspect.DownloadAspectChain;
import org.dromara.x.file.storage.core.aspect.FileStorageAspect;
import org.dromara.x.file.storage.core.aspect.UploadAspectChain;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.function.Consumer;

@Slf4j
@Component
public class LogFileStorageAspect implements FileStorageAspect {

    /**
     * 上传，成功返回文件信息，失败返回 null
     */
    @Override
    public FileInfo uploadAround(
            UploadAspectChain chain,
            FileInfo fileInfo,
            UploadPretreatment pre,
            FileStorage fileStorage,
            FileRecorder fileRecorder) {
        log.info("上传文件 before -> {}", fileInfo);
        fileInfo = chain.next(fileInfo, pre, fileStorage, fileRecorder);
        log.info("上传文件 after -> {}", fileInfo);
        return fileInfo;
    }
    /**
     * 删除文件，成功返回 true
     */
    @Override
    public boolean deleteAround(
            DeleteAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder) {
        log.info("删除文件 before -> {}", fileInfo);
        boolean res = chain.next(fileInfo, fileStorage, fileRecorder);
        log.info("删除文件 after -> {}", res);
        return res;
    }

    /**
     * 下载文件
     */
    @Override
    public void downloadAround(
            DownloadAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        log.info("下载文件 before -> {}", fileInfo);
        chain.next(fileInfo, fileStorage, consumer);
        log.info("下载文件 after -> {}", fileInfo);
    }

}
