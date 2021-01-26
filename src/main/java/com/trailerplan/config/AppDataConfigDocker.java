package com.trailerplan.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@WebAppConfiguration
@Profile("dev-local-bd-docker-pgsql")
@ComponentScan(basePackages = {"com.trailerplan.repository", "com.trailerplan.service", "com.trailerplan.controller"})
@EnableJpaRepositories(basePackages = {"com.trailerplan.repository"})
@EnableTransactionManagement
@EnableWebMvc
@EnableSwagger2
public class AppDataConfigDocker {

    @Inject
    private Environment environment;

    @Inject
    private ResourceLoader resourceLoader;

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceProvider(new HibernatePersistenceProvider());
        factory.setPersistenceUnitName("LOCAL_PERSISTENCE");
        factory.setJpaDialect(new HibernateJpaDialect());
        factory.setJpaVendorAdapter(jpaVendorAdapter());
        factory.setSharedCacheMode(SharedCacheMode.ALL);
        factory.setPackagesToScan(new String[] {"com.trailerplan.model.entity"});
        factory.setDataSource(dataSource());
        factory.setJpaProperties(hibernateProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setContextPath("/trailerplan");
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public HibernatePersistenceProvider persistenceProvider() {
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        return hibernatePersistenceProvider;
    }

    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapperFactoryBean dozerBeanTest() throws IOException {
        DozerBeanMapperFactoryBean dozerBean = new DozerBeanMapperFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver()
            .getResources("classpath*:dozer/**/*.dzr.xml");
        dozerBean.setMappingFiles(resources);
        return dozerBean;
    }


    /**
     * @apiNote in the url, the host container-postgres is the postgres container name defined in docker-compose.yml
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://container-postgres:5432/trailerplan");
        driver.setUsername("postgres");
        driver.setPassword("password");
        return driver;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        properties.setProperty("jadira.usertype.databaseZone", "jvm");
        properties.setProperty("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        return properties;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        return vendorAdapter;
    }

    @Bean
    public UndertowServletWebServerFactory embeddedServletContainerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
            @Override
            public void customize(io.undertow.Undertow.Builder builder) {
                builder.addHttpListener(8081, "0.0.0.0");
            }
        });
        return factory;
    }

}