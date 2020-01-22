package com.tzxx.webserver.preview.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/** url、localFilePath、previewType、platform这四个是url后?拼接的参数
 * previewType、platform可不传
 * 网络文件 传 url
 * 本地文件 传 localFilePath
 * @author zhangliang
 * @date 2020/1/14.
 */
@Data
public class SourceFileInfo {

    /**
     * 网络文件地址
     */
    private String url;
    /**
     * 本地文件地址
     */
    private String localFilePath;
    /**
     * 文件转换类型 不一定支持此类型且只针对office文件
     */
    private String previewType;
    /**
     * 平台 eg:web和其它
     */
    private String platform;

    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件名称带后缀
     */
    private String name;
    /**
     * 文件名称不带后缀
     */
    private String simpleName;

    public String getLocalFileDir(){
        return localFilePath.substring(0, localFilePath.lastIndexOf('/') + 1);
    }
    public String getLocalFileName(){
        return localFilePath.substring(localFilePath.lastIndexOf('/')+1);
    }

    public String getLocalFileSimpleName(){
        return localFilePath.substring(localFilePath.lastIndexOf('/')+1,localFilePath.lastIndexOf('.'));
    }

    public String getLocalFileType(){
        return localFilePath.substring(localFilePath.lastIndexOf('.')+1);
    }

    public boolean isLocal() {
        return StringUtils.isNotBlank(localFilePath);
    }


    public String getOfficeDefaultPreviewType() {
        if (FileTypeEnum.XLS.getName().equalsIgnoreCase(this.getType())
                || FileTypeEnum.XLS_X.getName().equalsIgnoreCase(this.getType())
                || FileTypeEnum.DOC.getName().equalsIgnoreCase(this.getType())
                || FileTypeEnum.DOC_X.getName().equalsIgnoreCase(this.getType())){
            return "html";
        }

        if (FileTypeEnum.PPT.getName().equalsIgnoreCase(this.getType())
                || FileTypeEnum.PPT_X.getName().equalsIgnoreCase(this.getType())){
            return "pdf";
        }
        return null;
    }
}
