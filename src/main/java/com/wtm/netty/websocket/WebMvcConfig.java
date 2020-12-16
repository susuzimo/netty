package com.wtm.netty.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
*WebMvcConfigurer配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter。基于java-based方式的spring mvc配置，需要创建一个配置类并实现WebMvcConfigurer 接口
 */
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {
	
	 @Override
	   public void addViewControllers(ViewControllerRegistry registry) {
	       registry.addViewController("/ws").setViewName("/ws");
	   }

}
