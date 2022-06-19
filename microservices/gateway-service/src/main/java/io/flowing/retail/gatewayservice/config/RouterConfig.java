package io.flowing.retail.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/orderservice/**")
                        .filters(f -> f.rewritePath("/orderservice/(?<path>.*)", "/$\\{path}"))
                        .uri("http://order-service:8091"))
                .build();

    }
}
