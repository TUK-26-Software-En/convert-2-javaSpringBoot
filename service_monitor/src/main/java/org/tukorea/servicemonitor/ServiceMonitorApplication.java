package org.tukorea.servicemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServiceMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceMonitorApplication.class, args);
    }
}
