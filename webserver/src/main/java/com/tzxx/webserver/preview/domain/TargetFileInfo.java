package com.tzxx.webserver.preview.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhangliang
 * @date 2020/1/14.
 */
@Data
@Builder
public class TargetFileInfo {
    private String previewPath;
}
