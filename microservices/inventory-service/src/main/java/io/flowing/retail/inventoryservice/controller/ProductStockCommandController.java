package io.flowing.retail.inventoryservice.controller;

import io.flowing.retail.inventoryservice.dto.ProductStockCommand;
import io.flowing.retail.inventoryservice.service.ProductStockCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@Tag(
        name = "Product stock",
        description = "Operations with product stock"
)
@Log4j2
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
