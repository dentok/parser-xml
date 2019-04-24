package com.dentok.parserxml.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@ComponentScan("com.dentok.parserxml")
public class Config {

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * The data source used to perform operations with DB.
     *
     * This {@code DataSource} bean is used for {@code @Profile} "development"
     *
     * @return {@code DataSource} bean
     */
    @Bean
    @Profile("development")
    @ConfigurationProperties(prefix = "application.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * The data source used to perform operations with DB.
     * This {@code DataSource} bean is used for "production" profile
     *
     * @return {@code DataSource} bean
     */
    @Bean
    @Profile("production")
    @ConfigurationProperties(prefix = "application.datasource")
    public DataSource primaryDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource(environment.getProperty("ibs.datasource.jndi-name.application"));
    }
}
