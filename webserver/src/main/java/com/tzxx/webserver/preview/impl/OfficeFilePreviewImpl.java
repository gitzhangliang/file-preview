package com.tzxx.webserver.preview.impl;

import com.tzxx.jodconverter.service.FileConverterServer;
import com.tzxx.webserver.preview.AbstractGenericFilePreview;
import com.tzxx.webserver.preview.domain.FileTypeEnum;
import com.tzxx.webserver.preview.domain.SourceFileInfo;
import com.tzxx.webserver.preview.domain.TargetFileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author zhangliang
 * @date 2019/10/16.
 */
@Service
public class OfficeFilePreviewImpl extends AbstractGenericFilePreview {

    @Resource
    private FileConverterServer fileConverterServer;

    @Override
    protected void converter(SourceFileInfo fileInfo) {
        String downLoadDir = getSourceDir(fileInfo);
        String previewDir = getTargetDir(fileInfo);
        String originalFileName = fileInfo.getName();
        String outName = getOutName(fileInfo);
        fileConverterServer.fileConverter(downLoadDir + originalFileName, previewDir + outName);
        String type = outName.substring(outName.lastIndexOf('.') + 1);
        if (FileTypeEnum.PDF.getName().equals(type) && pdf2Image) {
            pdfToImage(previewDir,previewDir, outName, filePreviewDir);
        }
    }

    @Override
    protected String getOutName(SourceFileInfo fileInfo) {
        String officePreviewType = fileInfo.getPreviewType();
        if (StringUtils.isBlank(officePreviewType)) {
            officePreviewType = fileInfo.getOfficeDefaultPreviewType();
        }
        String originalFileName = fileInfo.getName();
        return originalFileName.substring(0, originalFileName.lastIndexOf('.') + 1) + officePreviewType;
    }

    @Override
    protected TargetFileInfo createPreviewHandleResult(SourceFileInfo fileInfo) {
        String previewPath;
        if (WEB_PLATFORM.equals(fileInfo.getPlatform())) {
            previewPath = filePreviewDir + getCustomTargetDir(fileInfo) + getOutName(fileInfo);
        } else {
            previewPath = filePreviewDir + getCustomTargetDir(fileInfo) + fileInfo.getSimpleName() + ".html";
        }
        return TargetFileInfo.builder().previewPath(previewPath).build();
    }
}
