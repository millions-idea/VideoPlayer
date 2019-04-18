package com.management.admin;

import com.management.admin.config.WebLogAspectConfiguration;
import com.management.admin.utils.SpringApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
public class ManagementApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(ManagementApplication.class, args);
		System.out.println("启动内置tomcat服务成功!");
	}

	@Bean
	public WebLogAspectConfiguration getWebLogAspectConfiguration(){
		return new WebLogAspectConfiguration(true);
	}

	@Bean
	public SpringApplicationContext getSpringApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("2046MB");
		factory.setMaxRequestSize("2046MB");
		return factory.createMultipartConfig();
	}
}
