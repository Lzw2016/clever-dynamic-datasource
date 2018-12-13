package org.clever.dynamic.datasource.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-12-13 21:03 <br/>
 */
@Configuration
public class DynamicDatasourceWebAppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThreadLocalRemoveInterceptor()).addPathPatterns("/**");
    }
}
