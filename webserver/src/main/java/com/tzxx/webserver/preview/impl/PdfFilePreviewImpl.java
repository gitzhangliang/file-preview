package com.tzxx.webserver.preview.impl;

import com.tzxx.webserver.preview.AbstractGenericFilePreview;
import com.tzxx.webserver.preview.domain.SourceFileInfo;
import com.tzxx.webserver.preview.domain.TargetFileInfo;
import org.springframework.stereotype.Service;

/**
 * @author zhangliang
 * @date 2019/10/16.
 */
@Service
public class PdfFilePreviewImpl extends AbstractGenericFilePreview {

    @Override
    protected String getOutName(SourceFileInfo fileInfo) {
        return fileInfo.getName();
    }

    @Override
    protected void converter(SourceFileInfo fileInfo) {
        if (pdf2Image) {
            pdfToImage(getTargetDir(fileInfo),getSourceDir(fileInfo),fileInfo.getName(),filePreviewDir);
        }
    }

    @Override
    protected TargetFileInfo createPreviewHandleResult(SourceFileInfo fileInfo) {
        String previewPath;
        if(WEB_PLATFORM.equals(fileInfo.getPlatform())){
            previewPath = filePreviewDir+getCustomTargetDir(fileInfo)+getOutName(fileInfo);
        }else {
            previewPath = filePreviewDir+getCustomTargetDir(fileInfo)+fileInfo.getSimpleName()+".html";
        }
        return TargetFileInfo.builder().previewPath(previewPath).build();
    }
}
