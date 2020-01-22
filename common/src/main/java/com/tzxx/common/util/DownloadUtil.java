package com.tzxx.common.util;

import com.tzxx.common.exception.FileException;

import com.tzxx.common.exception.UrlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author yudian-it
 */
@Component
@Slf4j
public class DownloadUtil {

    public void downLoad(String urlAddress, String outPath, String outFileName) {
        log.info("下载urlAddress{}",urlAddress);
        URL url;
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            log.error("downLoad_file_URL_error_MalformedURLException:{0}:",e);
            throw new UrlException("创建URL出错");
        }
        String realPath = outPath + outFileName;
        File dirFile = new File(outPath);
        if (!dirFile.exists()) {
            boolean mkdirs = dirFile.mkdirs();
            if(!mkdirs){
                throw new UrlException("创建文件夹出错");
            }
        }
        URLConnection connection;
        try {
            connection = url.openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
        } catch (IOException e) {
            log.error("file_download_URLConnection_error:{0}",e);
            throw new UrlException("URLConnection出错");
        }
        log.info("下载生成的realPath{}",realPath);
        try (InputStream in = connection.getInputStream();
             FileOutputStream os = new FileOutputStream(realPath)){
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            log.error("file_download_error:{0}",e);
            throw new UrlException("文件下载出错");
        }
    }

    public void downLoadForWindowsServer(String urlAddress, String outPath, String outFileName) {
        log.info("下载urlAddress{}",urlAddress);
        String realPath = outPath + outFileName;
        File dirFile = new File(outPath);
        if (!dirFile.exists()) {
            boolean mkdirs = dirFile.mkdirs();
            if(!mkdirs){
                throw new UrlException("创建文件夹出错");
            }
        }
        try {
            File file = new File(realPath);
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(urlAddress);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            try (InputStream is = entity.getContent();
                 FileOutputStream fileOutputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[1024];
                int ch;
                while ((ch = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, ch);
                }
                fileOutputStream.flush();
            }
        } catch (Exception e) {
            log.error("文件下载失败{0}",e);
            throw new FileException("文件下载失败");
        }
    }
}
