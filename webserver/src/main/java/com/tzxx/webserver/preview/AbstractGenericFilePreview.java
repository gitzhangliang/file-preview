package com.tzxx.webserver.preview;

import com.tzxx.common.util.DownloadUtil;
import com.tzxx.webserver.cache.CacheService;
import com.tzxx.webserver.preview.domain.SourceFileInfo;
import com.tzxx.webserver.preview.domain.TargetFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**通用的文件预览
 * 对于本地文件  不需要转换的就不需要调用此服务 eg:jpg txt...
 * 对于网络文件 先下载，再转换
 * @author zhangliang
 * @date 2020/1/14.
 */
@Slf4j
public abstract class AbstractGenericFilePreview extends AbstractFilePreview<TargetFileInfo, SourceFileInfo> {

    @Value("${open_cache_previewFile_max}")
    protected boolean openCachePreviewFileMax;

    @Value("${open_LRU}")
    protected boolean openLru;

    @Value("${redis_cache_pre}")
    protected String cachePre;

    @Value("${cache_previewFile_max}")
    int cacheMax;

    @Resource
    private CacheService cacheService;

    @Resource
    private DownloadUtil downloadUtil;


    private void downLoad(String url, String originalName, String storagePath) {
        if(!new File(storagePath+originalName).exists()){
            downloadUtil.downLoad(url,storagePath,originalName);
        }
    }

    @Override
    protected String getSourceDir(SourceFileInfo sourceFileInfo) {
        if (!sourceFileInfo.isLocal()) {
            return super.getSourceDir(sourceFileInfo);
        }
        return sourceFileInfo.getLocalFileDir();
    }


    @Override
    protected String getCustomSourceDir(SourceFileInfo sourceFileInfo) {
        return sourceFileInfo.getSimpleName()+"/";
    }

    @Override
    protected String getCustomTargetDir(SourceFileInfo sourceFileInfo) {
        return sourceFileInfo.getSimpleName()+"/";
    }

    @Override
    protected void fileConverter(SourceFileInfo sourceFileInfo) {
        if (!sourceFileInfo.isLocal()) {
            downLoad(sourceFileInfo.getUrl(), sourceFileInfo.getName(), getSourceDir(sourceFileInfo));
        }
        converter(sourceFileInfo);
    }

    /**文件转换
     * @param sourceFileInfo 源文件信息
     */
    protected abstract void converter(SourceFileInfo sourceFileInfo);

    /**
     * 文件转换后缓存起来并且如果超出最多缓存数量，则删除最早生成的文件
     * @param outName 文件转换后的名称
     */
    private void cache(String outName){
        cacheService.zSet(cachePre,outName,Double.parseDouble(String.valueOf(System.currentTimeMillis())));
        if(openCachePreviewFileMax && cacheService.zSetCount(cachePre) > cacheMax){
            removeEarliestFile();
        }
    }

    /**
     * 删除最早生成的预览文件夹及其下文件
     */
    private void removeEarliestFile() {
        Object v = cacheService.zSetMinScore(cachePre);
        cacheService.zSetRemove(cachePre,v);
        String fileName = v.toString();
        String simpleName = fileName.substring(0,fileName.lastIndexOf('.'));
        removeFile(new File(previewStorageFileDir + simpleName));
    }

    /**
     * 删除文件夹及其下文件
     */
    private void removeFile(File file){
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                for (File f : files) {
                    removeFile(f);
                }
            }
        }
        try {
            Files.delete(Paths.get(file.toURI()));
        } catch (IOException e) {
            log.error("删除文件出错",e);
        }
    }

    @Override
    void afterPreviewSuccess(SourceFileInfo fileInfo) {
        super.afterPreviewSuccess(fileInfo);
        cache(getOutName(fileInfo));
    }

    @Override
    void afterPreviewExist(SourceFileInfo fileInfo) {
        super.afterPreviewExist(fileInfo);
        boolean contains = cacheService.zSetContains(cachePre, getOutName(fileInfo));
        if (openLru || !contains){
            cacheService.zSet(cachePre,getOutName(fileInfo),Double.parseDouble(String.valueOf(System.currentTimeMillis())));
        }
    }

    @Override
    void afterPreviewError(SourceFileInfo fileInfo) {
        super.afterPreviewError(fileInfo);
        log.error("文件名为{}的文件预览出错",fileInfo.getName());
    }
}
