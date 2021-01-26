package com.trailerplan.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Profile("dev-local-bd-memory-hsql")
@ComponentScan(basePackages = {"com.trailerplan.repository", "com.trailerplan.service", "com.trailerplan.controller"})
@EnableJpaRepositories(basePackages = {"com.trailerplan.repository"}, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class AppIntegrationTestDataConfigMemory {

    @Inject
    private Environment environment;

    @Inject
    private ResourceLoader resourceLoader;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());

        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setPersistenceUnitName("LOCAL_PERSISTENCE");
        entityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactoryBean.setSharedCacheMode(SharedCacheMode.ALL);
        entityManagerFactoryBean.setPackagesToScan(new String[] {"com.trailerplan.model.entity"});
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean;
    }

    //test IT rest assured : Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'javax.persistence.EntityManagerFactory' available: expected single matching bean but found 2: entityManagerFactory,sessionFactory
    /*@Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.trailerplan.model.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }*/


    @Bean
    @Primary
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.HSQL)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .addScript("database/hsql/hsql-schema.sql")
            .build();
    }

    @Bean
    public DataSourceInitializer nonBootDataSourceInitializer(DataSource dataSource, ResourceLoader resourceLoader) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setIgnoreFailedDrops(true);
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        databasePopulator.addScript(new ClassPathResource("database/hsql/hsql-data.sql"));
        return dataSourceInitializer;
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
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

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        properties.setProperty("jadira.usertype.databaseZone", "jvm");
        properties.setProperty("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        return properties;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(Database.HSQL);
        return vendorAdapter;
    }


    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }


}