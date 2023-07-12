package io.flowing.retail.inventoryservice.controller;

import io.flowing.retail.inventoryservice.dto.ProductStockQuery;
import io.flowing.retail.inventoryservice.service.ProductStockQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Log4j2
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
