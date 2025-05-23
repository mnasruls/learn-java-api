package javabasicapi.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javabasicapi.restful.middleware.ApiToken;

@Configuration
public class ApiConfiguration implements WebMvcConfigurer {
    @Autowired
    private ApiToken apiTokenMiddleware;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(apiTokenMiddleware);
    }
}
