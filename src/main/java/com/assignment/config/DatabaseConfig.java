package com.assignment.config;

import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    Query query(final DataSource dataSource) {
        Flyway.configure().dataSource(dataSource).load().migrate();
        return new FluentJdbcBuilder()
                .connectionProvider(dataSource)
                .build()
                .query();
    }
}
