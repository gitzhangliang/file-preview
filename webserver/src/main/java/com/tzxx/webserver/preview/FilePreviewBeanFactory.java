package com.tzxx.webserver.preview;

import com.tzxx.webserver.preview.domain.PreviewBeanEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


/**
 * @author tzxx
 */
@Service
public class FilePreviewBeanFactory implements ApplicationContextAware {
    @Value("${image_file}")
    private String imageFile;
    @Value("${compress_file}")
    private String compressFile;
    @Value("${office_file}")
    private String officeFile;
    @Value("${simText_file}")
    private String simTextFile;
    @Value("${media_file}")
    private String mediaFile;
    private static final String PDF = "pdf";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    private ApplicationContext context;

    public FilePreview get(String fileType) {
        Map<String, FilePreview> filePreviewMap = context.getBeansOfType(FilePreview.class);
        return filePreviewMap.get(Objects.requireNonNull(getFileType(fileType)).getInstanceName());
    }

    private PreviewBeanEnum getFileType(String fileType){
        final String spit = ",";
        if (Arrays.asList(imageFile.split(spit)).contains(fileType.toLowerCase())) {
            return PreviewBeanEnum.PICTURE;
        }
        if (Arrays.asList(officeFile.split(spit)).contains(fileType.toLowerCase())) {
            return PreviewBeanEnum.OFFICE;
        }
        if (Arrays.asList(simTextFile.split(spit)).contains(fileType.toLowerCase())) {
            return PreviewBeanEnum.SIM_TEXT;
        }
        if(PDF.equalsIgnoreCase(fileType)){
            return PreviewBeanEnum.PDF;
        }
        return null;
    }
}
