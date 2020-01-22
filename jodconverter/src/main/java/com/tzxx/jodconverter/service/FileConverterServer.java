package com.tzxx.jodconverter.service;

import com.tzxx.common.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.DocumentConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author zhangliang
 * @date 2019/8/15.
 */
@Component
@Slf4j
public class FileConverterServer {
    @Resource
    private DocumentConverter documentConverter;

    public void fileConverter(String source, String target){

        try {
            log.info("------openOffice开始转换 -------------"+source+"------"+target);
            documentConverter.convert(new File(source)).to(new File(target)).execute();
            log.info("------openOffice转换结束 -------------"+source+"------"+target);
        } catch (Exception e) {
            log.error("openOffice转换文件失败:{0}",e);
            throw new FileException("文件转换出错");
        }
    }
}
