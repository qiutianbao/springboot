//package com.nfcm.thymeleaf.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//import org.thymeleaf.templateresolver.UrlTemplateResolver;
//
////@Configuration
//public class ThymeleafConfig {
//
//	/**
//	 * 设置视图解析器
//	 * @param templateEngine
//	 * @return
//	 */
//	@Bean
//	public ViewResolver viewResolver(SpringTemplateEngine templateEngine){
//		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//		resolver.setTemplateEngine(templateEngine);
//		resolver.setCharacterEncoding("utf-8");
//		return resolver;
//	}
//
//	/**
//	 * 设置模板引擎
//	 * @param templateResolver
//	 * @return
//	 */
//	@Bean
//	public SpringTemplateEngine templateEngine(UrlTemplateResolver templateResolver){
//		SpringTemplateEngine engine = new SpringTemplateEngine();
//		engine.setTemplateResolver(templateResolver);
//		return engine;
//	}
//
//	/**
//	 * 模板解析引擎
//	 * @return
//	 */
//	@Bean
//	public UrlTemplateResolver templateResolver(){
//		UrlTemplateResolver resolver = new UrlTemplateResolver();
//		resolver.setPrefix("/WEB-INF/template/");//设置地址前缀
//		resolver.setSuffix(".html");//设置后缀
//		resolver.setCacheable(false);//设置不缓存
//		resolver.setCharacterEncoding("utf-8");
//		resolver.setTemplateMode("HTML5");
//		return resolver;
//
//	}
//
//}
