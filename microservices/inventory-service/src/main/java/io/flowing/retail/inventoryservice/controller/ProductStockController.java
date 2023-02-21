package io.flowing.retail.inventoryservice.controller;

import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import io.flowing.retail.inventoryservice.controller.payload.ProductStockBatchRequest;
import io.flowing.retail.inventoryservice.service.ProductStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
public class ProductStockController {

    private final ProductStockService productStockService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<ProductStockDTO>> getList(@RequestParam(name = "productId", required = false) Integer productId,
                                                         @RequestParam(name = "warehouseId", required = false) Integer warehouseId,
                                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(productStockService.getAll(productId, warehouseId, page, size));
    }

    @DeleteMapping
    @Operation(summary = "Delete stock (batch)")
    public void batchDelete(@RequestBody ProductStockBatchRequest request) {
        productStockService.batchDelete(request.getStocks());
    }

    @PutMapping
    @Operation(summary = "Update stocks (batch")
    public void batchUpdate(@RequestBody ProductStockBatchRequest request) {
        productStockService.batchUpdate(request.getStocks());
    }


}
