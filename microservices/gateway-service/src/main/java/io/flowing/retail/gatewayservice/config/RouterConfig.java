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
                        .path("/ordersvc/**")
                        .filters(f -> f.rewritePath("/ordersvc/(?<path>.*)", "/$\\{path}"))
                        .uri(orderService))
                .route(p -> p
                        .path("/paymentsvc/**")
                        .filters(f -> f.rewritePath("/paymentsvc/(?<path>.*)", "/$\\{path}"))
                        .uri(paymentService))
                .route(p -> p
                        .path("/inventorysvc/**")
                        .filters(f -> f.rewritePath("/inventorysvc/(?<path>.*)", "/$\\{path}"))
                        .uri(inventoryService))
                .route(p -> p
                        .path("/customersvc/**")
                        .filters(f -> f.rewritePath("/customersvc/(?<path>.*)", "/$\\{path}"))
                        .uri(customerService))
                .route(p -> p
                        .path("/productsvc/**")
                        .filters(f -> f.rewritePath("/productsvc/(?<path>.*)", "/$\\{path}"))
                        .uri(productService))
                .route(p -> p
                        .path("/shippingsvc/**")
                        .filters(f -> f.rewritePath("/shippingsvc/(?<path>.*)", "/$\\{path}"))
                        .uri(shippingService))
                .build();

    }
}
