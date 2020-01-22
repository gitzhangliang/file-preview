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
public class SimTextFilePreviewImpl extends AbstractGenericFilePreview {
    @Override
    protected String getOutName(SourceFileInfo fileInfo) {
        return fileInfo.getName();
    }

    @Override
    protected void converter(SourceFileInfo fileInfo) {
        //简单文本不需要转换
    }

    @Override
    protected TargetFileInfo createPreviewHandleResult(SourceFileInfo fileInfo) {
        return TargetFileInfo.builder().previewPath(filePreviewDir+getCustomTargetDir(fileInfo)+fileInfo.getName()).build();
    }
}
