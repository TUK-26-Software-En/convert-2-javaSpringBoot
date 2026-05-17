package org.tukorea.libcheckout.global.business.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("docker-pg")
public class DockerDemoDataSqlInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    public DockerDemoDataSqlInitializer(
            JdbcTemplate jdbcTemplate,
            DataSource dataSource,
            ResourceLoader resourceLoader
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!isInitialDatabaseState()) {
            return;
        }

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                resourceLoader.getResource("classpath:db/seed/demo-data-postgres.sql")
        );
        populator.execute(dataSource);
    }

    private boolean isInitialDatabaseState() {
        return tableRowCount("books") == 0L
                && tableRowCount("members") == 0L
                && tableRowCount("loans") == 0L;
    }

    private long tableRowCount(String tableName) {
        Long count = jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
        return count == null ? 0L : count;
    }
}
