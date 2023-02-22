package io.flowing.retail.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    @Value("${routes.uri.order-service}")
    private String orderService;

    @Value("${routes.uri.payment-service}")
    private String paymentService;

    @Value("${routes.uri.inventory-service}")
    private String inventoryService;

    @Value("${routes.uri.customer-service}")
    private String customerService;

    @Value("${routes.uri.product-service}")
    private String productService;

    @Value("${routes.uri.shipping-service}")
    private String shippingService;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/order/**")
                        .filters(f -> f.rewritePath("/api/v1/order/(?<path>.*)", "/$\\{path}"))
                        .uri(orderService))
                .route(p -> p
                        .path("/api/v1/payment/**")
                        .filters(f -> f.rewritePath("/api/v1/payment/(?<path>.*)", "/$\\{path}"))
                        .uri(paymentService))
                .route(p -> p
                        .path("/api/v1/stock/**")
                        .filters(f -> f.rewritePath("/api/v1/stock/(?<path>.*)", "/$\\{path}"))
                        .uri(inventoryService))
                .route(p -> p
                        .path("/api/v1/customer/**")
                        .filters(f -> f.rewritePath("/api/v1/customer/(?<path>.*)", "/$\\{path}"))
                        .uri(customerService))
                .route(p -> p
                        .path("/api/v1/product/**")
                        .filters(f -> f.rewritePath("/api/v1/product/(?<path>.*)", "/$\\{path}"))
                        .uri(productService))
                .route(p -> p
                        .path("/api/v1/shipping/**")
                        .filters(f -> f.rewritePath("/api/v1/shipping/(?<path>.*)", "/$\\{path}"))
                        .uri(shippingService))
                .build();

    }
}
