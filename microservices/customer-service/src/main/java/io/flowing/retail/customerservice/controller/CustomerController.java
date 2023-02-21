package io.flowing.retail.customerservice.controller;

import io.flowing.retail.customerservice.dto.CustomerDTO;
import io.flowing.retail.customerservice.service.CustomerService;
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

@Validated
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(
        name = "Customer",
        description = "Operations with payments"
)
@Log4j2
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<CustomerDTO>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(customerService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id")
    public ResponseEntity<CustomerDTO> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
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
                       @Valid @RequestBody CustomerDTO dto) {
        customerService.update(id, dto);
    }

}
