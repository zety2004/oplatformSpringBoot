package com.hklk.oplatform.config;

import com.hklk.oplatform.filter.LocalLoginInterceptor;
import com.hklk.oplatform.filter.SchoolLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {


    @Bean
    public LocalLoginInterceptor getLocalLoginInterceptor() {
        return new LocalLoginInterceptor();
    }

    @Bean
    public SchoolLoginInterceptor getSchoolLoginInterceptor() {
        return new SchoolLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocalLoginInterceptor localLoginInterceptor = getLocalLoginInterceptor();
        SchoolLoginInterceptor schoolLoginInterceptor = getSchoolLoginInterceptor();
        registry.addInterceptor(localLoginInterceptor).addPathPatterns("/**");
        registry.addInterceptor(schoolLoginInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}