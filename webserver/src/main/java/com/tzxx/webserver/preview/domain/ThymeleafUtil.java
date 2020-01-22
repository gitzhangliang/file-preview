package com.tzxx.webserver.preview.domain;

import com.tzxx.common.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author zhangliang
 * @date 2019/10/16.
 */
@Slf4j
public class ThymeleafUtil {
    private ThymeleafUtil(){}
    public static void constructHtml(Map<String,Object> dataMap, String dir, String name,String templateName){
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //构造上下文(Model)
        Context context = new Context();
        dataMap.forEach(context::setVariable);

        //渲染模板
        try(FileWriter write = new FileWriter(dir+name+".html")) {
            templateEngine.process(templateName, context, write);
        } catch (IOException e) {
            log.error("Thymeleaf生成html出错");
            throw new FileException(e.getMessage());
        }
    }
}
