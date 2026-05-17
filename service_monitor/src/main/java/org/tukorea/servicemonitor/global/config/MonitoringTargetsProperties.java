package org.tukorea.servicemonitor.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "monitoring.targets")
public class MonitoringTargetsProperties {

    private String libraryServiceHealthUrl = "http://localhost:8080/actuator/health";
    private String postgresJdbcUrl = "jdbc:postgresql://localhost:5432/lib_checkout";
    private String postgresUsername = "lib_checkout";
    private String postgresPassword = "lib_checkout";

    public String getLibraryServiceHealthUrl() {
        return libraryServiceHealthUrl;
    }

    public void setLibraryServiceHealthUrl(String libraryServiceHealthUrl) {
        this.libraryServiceHealthUrl = libraryServiceHealthUrl;
    }

    public String getPostgresJdbcUrl() {
        return postgresJdbcUrl;
    }

    public void setPostgresJdbcUrl(String postgresJdbcUrl) {
        this.postgresJdbcUrl = postgresJdbcUrl;
    }

    public String getPostgresUsername() {
        return postgresUsername;
    }

    public void setPostgresUsername(String postgresUsername) {
        this.postgresUsername = postgresUsername;
    }

    public String getPostgresPassword() {
        return postgresPassword;
    }

    public void setPostgresPassword(String postgresPassword) {
        this.postgresPassword = postgresPassword;
    }
}
