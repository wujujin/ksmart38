package ksmart38.mybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart38.mybatis.interceptor.CommonInterceptor;
import ksmart38.mybatis.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	
	private final CommonInterceptor commonInerceptor ;
	private final LoginInterceptor loginInterceptor ;
	
	public WebConfig(CommonInterceptor commonInerceptor, LoginInterceptor loginInterceptor) {
		this.commonInerceptor = commonInerceptor;
		this.loginInterceptor = loginInterceptor;
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInerceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/css/**");
				
		
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/ajax/**")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/login")
				.excludePathPatterns("/logout");
	}
	 
     
     
}
