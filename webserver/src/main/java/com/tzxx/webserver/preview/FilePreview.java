package com.tzxx.webserver.preview;

/**
 * @author zhangliang
 * @date 2019/10/16.
 */
public interface FilePreview<T,R> {
    /**预览
     * @param r 源文件信息
     * @return 生成的预览文件信息
     */
    T preview(R r);
}
