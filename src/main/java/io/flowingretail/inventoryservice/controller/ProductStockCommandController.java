package io.flowingretail.inventoryservice.controller;

import io.flowingretail.inventoryservice.dto.ProductStockCommand;
import io.flowingretail.inventoryservice.service.ProductStockCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
//for WRITE controller
//@ConditionalOnProperty(name = "app.write.enabled", havingValue = "true")
public class ProductStockCommandController {

    private final ProductStockCommandService productStockCommandService;

    @DeleteMapping
    @Operation(summary = "Delete stock (batch)")
    public void batchDelete(@RequestBody ProductStockCommand request) {
        productStockCommandService.batchDelete(request.getStocks());
    }

    @PutMapping
    @Operation(summary = "Update stocks (batch")
    public void batchUpdate(@RequestBody ProductStockCommand request) {
        productStockCommandService.batchUpdate(request.getStocks());
    }


}
