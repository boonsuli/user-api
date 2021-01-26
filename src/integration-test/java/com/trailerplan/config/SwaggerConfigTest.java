package com.trailerplan.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * http://localhost:8080/trailerplan/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfigTest {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfigTest.class);

    private static final String SWAGGER_DOC_ENTRYPOINT = "/api-docs";
    private static final String SWAGGER_UI_HOME = "/webjars/swagger-ui/2.2.10/index.html";


    @Bean
    public WebMvcConfigurer configurer() {

        return new WebMvcConfigurer() {

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
        };
    }

    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("trailerplan-api")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "trailerplan_api",
                "trailerplan_desc",
                "trailerplan_version",
                "terms of service",
                new Contact("Buon Sui", "www.websitetodo.com", "boonsuli@gmail.com"),
                "trailerplan_license",
                "api license url",
                Collections.emptyList());
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


    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

}
