package com.trailerplan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.trailerplan.repository", "com.trailerplan.service", "com.trailerplan.controller"})
@EnableJpaRepositories(basePackages = {"com.trailerplan.repository"})
@EnableTransactionManagement
public class AppDataConfig {

    @Inject
    private Environment environment;

    @Inject
    private ResourceLoader resourceLoader;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public Properties hibernateProperties;

}