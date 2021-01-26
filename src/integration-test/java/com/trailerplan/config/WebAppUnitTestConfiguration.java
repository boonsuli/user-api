package com.trailerplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WebAppUnitTestConfiguration implements WebMvcConfigurer {

    private static final String SWAGGER_DOC_ENTRYPOINT = "/api-docs";
    private static final String SWAGGER_UI_HOME = "/webjars/swagger-ui/2.2.10/index.html";


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Entrypoint for swagger-ui access
        registry
            .addViewController("/swagger-ui")
            .setViewName("forward:dispatch-swagger-ui.html");

        // Swagger UI def for new services
        registry
            .addViewController("/v2/swagger-ui")
            .setViewName("redirect:" + SWAGGER_UI_HOME + "?url="  + SWAGGER_DOC_ENTRYPOINT + "?group=newapi");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry
            .addResourceHandler("dispatch-swagger-ui.html")
            .addResourceLocations("classpath:/pages/dispatch-swagger-ui.html");
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "PUT", "POST", "DELETE")
            .allowedOrigins("*");
    }

    @Bean
    public RequestMappingHandlerAdapter requestHandler() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        adapter.getMessageConverters().add(converter);
        return adapter;
    }
}
