package com.qiguliuxing.dts.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/admin/**")
				.allowedOrigins("http://chaoshangshiduo.com")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
