package com.tzxx.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tzxx
 */
@Component
@Slf4j
public class PdfUtils {

    public List<String> pdf2jpg(String previewDir,String pdfDir,String pdfFileName,String imageUrlPrefix) {
        List<String> imageUrls = new ArrayList<>();
        try {
            String folder = pdfFileName.substring(0,pdfFileName.lastIndexOf('.'));
            File pdfFile = new File(pdfDir+pdfFileName);

            PDDocument doc = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();

            String imageFilePath;
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                imageFilePath = previewDir + pageIndex + ".jpg";
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 105, ImageType.RGB);
                ImageIOUtil.writeImage(image, imageFilePath, 105);
                log.info("folder ----- " +folder);
                imageUrls.add(imageUrlPrefix + folder +"/" + pageIndex + ".jpg");
            }
            doc.close();
        } catch (IOException e) {
            log.error("Convert pdf to jpg exception:{0}", e);
        }
        return imageUrls;
    }

    public List<String> getPdfJpgPath(String pdfDir,String pdfFileName,String imageUrlPrefix) {
        List<String> imageUrls = new ArrayList<>();
        try {
            String folder = pdfFileName.substring(0,pdfFileName.lastIndexOf('.'));
            File pdfFile = new File(pdfDir+pdfFileName);
            PDDocument doc = PDDocument.load(pdfFile);
            int pageCount = doc.getNumberOfPages();

            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                imageUrls.add(imageUrlPrefix +folder+"/" + pageIndex + ".jpg");
            }
            doc.close();
        } catch (IOException e) {
            log.error("Convert pdf to jpg exception:{0}", e);
        }
        return imageUrls;
    }
}
