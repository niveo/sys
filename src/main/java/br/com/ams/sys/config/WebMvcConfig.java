package br.com.ams.sys.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableAutoConfiguration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/*.js", "/*.css", "/*.ttf", "/*.woff", "/*.woff2", "/*.eot", "/*.svg", "/*.jpg")
				.addResourceLocations("classpath:/static/")
				.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS).cachePrivate().mustRevalidate())
				.resourceChain(true).addResolver(new PathResourceResolver());

		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/")
				.setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS).cachePrivate().mustRevalidate())
				.resourceChain(true).addResolver(new PathResourceResolver());

	}

	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		var messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setObjectMapper(objectMapper);
		return messageConverter;

	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jacksonMessageConverter());
		WebMvcConfigurer.super.configureMessageConverters(converters);
	}
}
