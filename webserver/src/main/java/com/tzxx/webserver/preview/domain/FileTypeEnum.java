package com.tzxx.webserver.preview.domain;

import lombok.Getter;

/**
 * @author zhangliang
 * @date 2020/1/14.
 */
@Getter
public enum  FileTypeEnum {
    //文件类型枚举
    XLS("xls"),
    XLS_X("xlsx"),
    DOC("doc"),
    DOC_X("docx"),
    PPT("ppt"),
    PPT_X("pptx"),
    PDF("pdf"),
    JPG("jpg"),
    JPEG("jpeg");
    private final String name;

    FileTypeEnum(String name){
        this.name = name;
    }
}
