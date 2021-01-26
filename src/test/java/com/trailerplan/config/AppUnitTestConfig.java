package com.trailerplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Configuration
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppUnitTestDataConfigMemory.class)
@ComponentScan(basePackages = {"com.trailerplan.controller", "com.trailerplan.service", "com.trailerplan.repository"})
@Import({AppUnitTestDataConfigMemory.class})
@EnableWebMvc
@EnableSwagger2
public class AppUnitTestConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:validation/message-validation-test");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    public javax.validation.Validator validator() {
        final LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource());
        return factory;
    }

    @Bean
    public Docket api() {

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
            "trailerplan-api",
            "trailerplan-desc",
            "trailerplan-version",
            "terms of service",
            new Contact("Buon Sui", "www.websitetodo.com", "boonsuli@gmail.com"),
            "trailerplan_license",
            "api license url",
            Collections.emptyList());
    }
}

