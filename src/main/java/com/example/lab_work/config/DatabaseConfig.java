package com.example.lab_work.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory() {
        return projectsDbEntityManagerFactory(projectsDbDataSource());
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Primary
    @Bean(name = "projectsDbDataSource")
    @ConfigurationProperties(prefix = "app.datasource.projects")
    public DataSource projectsDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "resumeDbDataSource")
    @ConfigurationProperties(prefix = "app.datasource.resume")
    public DataSource resumeDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataWarehouseDataSource")
    @ConfigurationProperties(prefix = "app.datasource.warehouse")
    public DataSource dataWarehouseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "projectsDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean projectsDbEntityManagerFactory(
            @Qualifier("projectsDbDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.lab_work.model.projects_db");
        emf.setPersistenceUnitName("projectsDb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(properties);

        return emf;
    }

    @Bean(name = "resumeDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean resumeDbEntityManagerFactory(
            @Qualifier("resumeDbDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.lab_work.model.resume_db");
        emf.setPersistenceUnitName("resumeDb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(properties);

        return emf;
    }

    @Bean(name = "dataWarehouseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dataWarehouseEntityManagerFactory(
            @Qualifier("dataWarehouseDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.lab_work.model.data_warehouse");
        emf.setPersistenceUnitName("dataWarehouse");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(properties);

        return emf;
    }

    @Bean(name = "projectsDbTransactionManager")
    public PlatformTransactionManager projectsDbTransactionManager(
            @Qualifier("projectsDbEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "resumeDbTransactionManager")
    public PlatformTransactionManager resumeDbTransactionManager(
            @Qualifier("resumeDbEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "dataWarehouseTransactionManager")
    public PlatformTransactionManager dataWarehouseTransactionManager(
            @Qualifier("dataWarehouseEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}