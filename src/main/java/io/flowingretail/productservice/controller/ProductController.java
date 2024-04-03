package io.flowingretail.productservice.controller;

import io.flowingretail.productservice.dto.ProductDto;
import io.flowingretail.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(
        name = "Product",
        description = "Operations with goods"
)
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get a list of products")
    public ResponseEntity<Page<ProductDto>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(productService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the product by the identifier")
    public ResponseEntity<ProductDto> getById(@Valid @NotNull @PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<String> save(@Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.save(dto).toString());
    }

    @PostMapping("/batch")
    @Operation(summary = "Create batch of new product")
    public ResponseEntity<String> saveBatch(@Valid @RequestBody List<ProductDto> dtoList) {
        productService.saveBatch(dtoList);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by id")
    public void delete(@Valid @NotNull @PathVariable("id") Integer id) {
        productService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public void update(@Valid @NotNull @PathVariable("id") Integer id,
                       @Valid @RequestBody ProductDto dto) {
        productService.update(id, dto);
    }


}
