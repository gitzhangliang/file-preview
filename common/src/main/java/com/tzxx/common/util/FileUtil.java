package com.tzxx.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangliang
 * @date 2019/8/15.
 */
@Slf4j
public class FileUtil {
    private FileUtil(){}

    public static String suffixFromUrl(String url) {
        String nonPramStr = url.substring(0, url.indexOf('?') != -1 ? url.indexOf('?') : url.length());
        String fileName = nonPramStr.substring(nonPramStr.lastIndexOf('/') + 1);
        return suffixFromFileName(fileName);
    }

    public static String suffixFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * 从url中剥离出文件名
     */
    public static String getFileNameFromUrl(String url) {
        String noQueryUrl = url.substring(0, url.indexOf('?') != -1 ? url.indexOf('?'): url.length());
        return noQueryUrl.substring(noQueryUrl.lastIndexOf('/') + 1);
    }

    /**
     * 获取url中的参数
     * @param url file net url
     * @return url parameter
     */
    private static Map<String, String> getUrlParameter(String url) {
        Map<String, String> mapRequest = new HashMap<>(16);
        String strAllParam = null;
        String[] arrSplit = url.split("[?]");
        if(arrSplit.length > 1 && arrSplit[1] != null) {
            strAllParam = arrSplit[1];
        }
        if (strAllParam != null) {
            //每个键值为一组
            arrSplit = strAllParam.split("[&]");
            for(String strSplit:arrSplit) {
                String[] arrSplitEqual = strSplit.split("[=]");
                //解析出键值
                if(arrSplitEqual.length > 1) {
                    mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
                }
            }
        }
        return mapRequest;
    }
}
