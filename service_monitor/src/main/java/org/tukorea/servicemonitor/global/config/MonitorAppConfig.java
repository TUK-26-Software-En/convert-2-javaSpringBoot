package org.tukorea.servicemonitor.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        MonitoringTargetsProperties.class,
        DockerControlProperties.class,
        FailureInjectionProperties.class
})
public class MonitorAppConfig {

    @Bean
    RestClient monitorRestClient(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3_000);
        requestFactory.setReadTimeout(3_000);
        return builder.requestFactory(requestFactory).build();
    }
}
