package top.alanlee.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.alanlee.template.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //配置跨域请求
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new JwtInterceptor());
        // 需拦截的路径
        interceptorRegistration.addPathPatterns("/**");
        // 需放行的路径
        interceptorRegistration.excludePathPatterns("/**/login", "/**/unlogin");
    }
}
