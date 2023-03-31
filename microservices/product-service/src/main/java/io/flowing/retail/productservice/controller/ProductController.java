package io.flowing.retail.productservice.controller;

import io.flowing.retail.productservice.dto.ProductDTO;
import io.flowing.retail.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(
        name = "Product",
        description = "Operations with goods"
)
@Log4j2
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get a list of products")
    public ResponseEntity<Page<ProductDTO>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(productService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the product by the identifier")
    public ResponseEntity<ProductDTO> getById(@Valid @NotNull @PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<String> save(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.save(dto).toString());
    }

    @PostMapping("/batch")
    @Operation(summary = "Create batch of new product")
    public ResponseEntity<String> saveBatch(@Valid @RequestBody List<ProductDTO> dtoList) {
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
                       @Valid @RequestBody ProductDTO dto) {
        productService.update(id, dto);
    }


}
