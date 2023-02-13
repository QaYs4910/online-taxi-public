package com.mashibing.apipassenger.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {


//
//    /**
//     * 定义Bean的初始化顺序
//     * @return JwtInterceptor()
//     */
//    @Bean
//    public JwtInterceptor jwtInterceptor(){
//        return new JwtInterceptor();
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                //需要拦截的路径
                .addPathPatterns("/**")   //拦截所有
                //不需要拦截的路径
                .excludePathPatterns("/noAuthTest"); //不需要拦截的路径

    }

}
