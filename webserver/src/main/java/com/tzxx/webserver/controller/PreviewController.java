package com.tzxx.webserver.controller;

import com.tzxx.common.util.FileUtil;
import com.tzxx.webserver.preview.AbstractGenericFilePreview;
import com.tzxx.webserver.preview.FilePreviewBeanFactory;
import com.tzxx.webserver.preview.domain.SourceFileInfo;
import com.tzxx.webserver.preview.domain.TargetFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangliang
 * @date 2020/1/14.
 */
@RestController
@Slf4j
public class PreviewController {
    @Resource
    private FilePreviewBeanFactory previewBeanFactory;

    @GetMapping(value = "/onlinePreviewJson")
    public TargetFileInfo onlinePreviewJson(String platform,String url,String previewType,String localFilePath) {
        log.info("previewFileUrl:{}",url);
        log.info("localFilePath:{}",localFilePath);
        SourceFileInfo sourceFileInfo = getSourceFileInfo(platform,url,previewType,localFilePath);
        AbstractGenericFilePreview filePreview = (AbstractGenericFilePreview)previewBeanFactory.get(sourceFileInfo.getType());
        return filePreview.preview(sourceFileInfo);
    }

    private SourceFileInfo getSourceFileInfo(String platform,String url,String previewType,String localFilePath){

        SourceFileInfo fileInfo = new SourceFileInfo();
        fileInfo.setLocalFilePath(localFilePath);
        fileInfo.setPreviewType(previewType);
        fileInfo.setUrl(url);
        fileInfo.setPlatform(platform);
        if (fileInfo.isLocal()) {
            fileInfo.setName(fileInfo.getLocalFileName());
            fileInfo.setSimpleName(fileInfo.getLocalFileSimpleName());
            fileInfo.setType(fileInfo.getLocalFileType());
        }else {
            fileInfo.setName(FileUtil.getFileNameFromUrl(url));
            fileInfo.setSimpleName(fileInfo.getName().substring(0,fileInfo.getName().lastIndexOf('.')));
            fileInfo.setType(fileInfo.getName().substring(fileInfo.getName().lastIndexOf('.') + 1));
        }

        return fileInfo;
    }

}
