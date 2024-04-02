package io.flowingretail.inventoryservice.controller;

import io.flowingretail.inventoryservice.dto.ProductStockQuery;
import io.flowingretail.inventoryservice.service.ProductStockQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@Tag(
        name = "Product stock",
        description = "Operations with product stock"
)
@Slf4j
//for READ controller
//@ConditionalOnProperty(name = "app.write.enabled", havingValue = "false")
public class ProductStockQueryController {

    private final ProductStockQueryService productStockQueryService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<ProductStockQuery> getList(@RequestParam(name = "productId", required = false) Integer productId,
                                                     @RequestParam(name = "warehouseId", required = false) Integer warehouseId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(new ProductStockQuery(productStockQueryService.getAll(productId, warehouseId, page, size)));
    }

}
