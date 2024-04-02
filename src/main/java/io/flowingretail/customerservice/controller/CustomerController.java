package io.flowingretail.customerservice.controller;

import io.flowingretail.customerservice.dto.CustomerDto;
import io.flowingretail.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(
        name = "Customer",
        description = "Operations with payments"
)
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<CustomerDto>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(customerService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id")
    public ResponseEntity<CustomerDto> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete by id")
    public void delete(@Valid @NotNull @PathVariable("id") Integer id) {
        customerService.delete(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public void update(@Valid @NotNull @PathVariable("id") Integer id,
                       @Valid @RequestBody CustomerDto dto) {
        customerService.update(id, dto);
    }

}
