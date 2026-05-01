package com.acme.order.bootstrap.spring.rest.config;

import java.net.HttpURLConnection;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TracingFallbackConfig {

    private static final Logger log = LoggerFactory.getLogger(TracingFallbackConfig.class);

    @Value("${management.zipkin.tracing.endpoint}")
    private String zipkinEndpoint;

    @Bean
    public CommandLineRunner checkZipkinAvailability() {
        return args -> {
            try {
                // Try to connect to Zipkin with a very brief timeout
                HttpURLConnection connection = (HttpURLConnection) new URI(zipkinEndpoint).toURL().openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                
                if (responseCode >= 200 && responseCode < 400) {
                    log.info("Zipkin server is available at {}. Tracing enabled.", zipkinEndpoint);
                } else {
                    log.warn("Zipkin server responded with code {}. Disabling tracing to keep application running seamlessly.", responseCode);
                    System.setProperty("management.tracing.enabled", "false");
                }
            } catch (Exception e) {
                log.warn("Could not connect to Zipkin server at {}. Reason: {}. Tracing will be disabled.", 
                        zipkinEndpoint, e.getMessage());
                // Disable tracing on the running context
                System.setProperty("management.tracing.enabled", "false");
            }
        };
    }
}