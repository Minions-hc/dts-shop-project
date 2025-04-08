package com.qiguliuxing.dts.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRootPath = System.getProperty("user.dir");
        String fileUrl = "file:" + Paths.get(projectRootPath).toAbsolutePath().toString() + "/dts/storage/";

        registry.addResourceHandler("/dts/storage/**")
                .addResourceLocations(fileUrl);
    }
}