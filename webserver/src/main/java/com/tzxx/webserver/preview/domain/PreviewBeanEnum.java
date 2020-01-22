package com.tzxx.webserver.preview.domain;

/**
 * @author zhangliang
 * @date 2020/1/14.
 */
public enum  PreviewBeanEnum {
    //
    PICTURE("pictureFilePreviewImpl"),
    OFFICE("officeFilePreviewImpl"),
    SIM_TEXT("simTextFilePreviewImpl"),
    PDF("pdfFilePreviewImpl");

    private String instanceName;
    PreviewBeanEnum(String instanceName){
        this.instanceName=instanceName;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
