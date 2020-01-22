package com.tzxx.webserver.preview;

import com.tzxx.common.exception.FileException;
import com.tzxx.common.exception.UrlException;
import com.tzxx.common.util.PdfUtils;
import com.tzxx.webserver.preview.domain.ThymeleafUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


/**preview包模块是对网络文件并发预览实现，简化了之前下载、生成预览文件、pdf转image分别并发的处理
 * 现在这三个操作合到一个并发操作里，对于不同文件预览实现所需的钩子函数也更为简便
 * @author zhangliang
 * @date 2019/10/16.
 */
@Slf4j
public abstract class AbstractFilePreview<T,R> implements FilePreview<T,R> {

    protected static final String WEB_PLATFORM = "web";

    @Value("${FILE_DOWNLOAD_STORAGE_PATH}")
    String downloadStorageFileDir;

    @Value("${FILE_PREVIEW_STORAGE_PATH}")
    String previewStorageFileDir;

    @Value("${FILE_PREVIEW_PATH}")
    protected String filePreviewDir;

    @Value("${pdf_to_image}")
    protected boolean pdf2Image;

    @Resource
    private PdfUtils pdfUtils;

    private final Map<String, CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>(16);


    protected String getSourceDir(R r) {
        return downloadStorageFileDir+getCustomSourceDir(r);
    }

    protected String getTargetDir(R r) {
        String targetDir = previewStorageFileDir + getCustomTargetDir(r);
        File dirFile = new File(targetDir);
        if (!dirFile.exists()) {
            boolean mkdirs = dirFile.mkdirs();
            if(!mkdirs){
                throw new UrlException("创建文件夹出错");
            }
        }
        return targetDir;
    }


    /**
     *预览文件生成成功
     * @param r 源文件信息
     */
    void afterPreviewSuccess(R r){

        log.error("预览文件生成成功{}",r.toString());
    }

    /**
     *预览文件生成失败
     * @param r 源文件信息
     */
     void afterPreviewError(R r){
         log.error("预览文件生成失败{}",r.toString());
     }

    /**
     *预览文件之前已经生成
     * @param r 源文件信息
     */
     void afterPreviewExist(R r){
         log.error("预览文件之前已经生成{}",r.toString());
     }

    /**
     * 源文件自定义目录
     * @param r 源文件信息
     * @return 自定义文件目录
     */
    protected abstract String getCustomSourceDir(R r);

    /**
     * 目标文件自定义目录
     * @param r 源文件信息
     * @return 自定义文件目录
     */
    protected abstract String getCustomTargetDir(R r);

    /**
     *文件转换
     * @param r 源文件信息
     */
    protected abstract void fileConverter(R r);

    /**
     *获取要生成预览文件名称（带文件后缀）
     * @param r 源文件信息
     * @return 生成的文件名
     */
    protected abstract String getOutName(R r);

    /**
     * 构造转换后结果
     * @param r 源文件信息
     * @return 转换后结果
     */
    protected abstract T createPreviewHandleResult(R r);


    /**
     * pdf转image，且把图片生成到html文件中
     * @param previewDir pdf文件路径
     * @param outName pdf文件名称
     * @param filePreviewDir 文件预览路径
     */
    protected void pdfToImage(String previewDir,String pdfDir,String outName, String filePreviewDir){
        List<String> images = pdfUtils.pdf2jpg(previewDir,pdfDir,outName, filePreviewDir);
        Map<String,Object> map = new HashMap<>(16);
        map.put("filePreviewDir",images);
        String name = outName.substring(0,outName.lastIndexOf('.'));
        ThymeleafUtil.constructHtml(map,previewDir,name,"pdfPicture");
    }

    @Override
    public T preview(R r) {
        String outName = getOutName(r);
        CountDownLatch downLatch = null;
        synchronized (this){
            if (countDownLatchMap.containsKey(outName)){
                downLatch = countDownLatchMap.get(outName);
            }else {
                File previewFile = new File(getTargetDir(r)+outName);
                if (previewFile.exists()){
                    //说明该线程执行此预览方法时已生成预览文件
                    afterPreviewExist(r);
                    return createPreviewHandleResult(r);
                }else {
                    countDownLatchMap.put(outName,new CountDownLatch(1));
                }
            }
        }
        //说明有线程也在预览该文件
        if (downLatch!=null){
            try {
                downLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }else {
            boolean success = false;
            try{
                fileConverter(r);
                success = true;
            }catch (Exception e){
                afterPreviewError(r);
                log.error("文件转换异常{0}",e);
                throw new FileException("文件转换异常");
            }finally {
                if (success){
                    afterPreviewSuccess(r);
                }
                countDownLatchMap.get(outName).countDown();
                synchronized (this){
                    countDownLatchMap.remove(outName);
                }
            }
        }
        return createPreviewHandleResult(r);
    }
}
