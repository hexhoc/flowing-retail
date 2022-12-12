package io.flowing.retail.customer.controller;

import io.flowing.retail.customer.entity.Customer;
import io.flowing.retail.customer.service.CustomerService;
import io.flowing.retail.customer.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
@Log
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Save ")
    public ResponseEntity<String> save(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Save customer");
        return ResponseEntity.ok(customerService.save(customerDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ")
    public void delete(@Valid @NotNull @PathVariable("id") String id) {
        log.info("Delete customer");
        customerService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ")
    public ResponseEntity<String> update(@Valid @NotNull @PathVariable("id") String id,
                       @Valid @RequestBody CustomerDto customerDto) {
        customerService.update(id, customerDto);
        log.info("Update customer");
        return ResponseEntity.ok("Successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve by ID ")
    public CustomerDto getById(@Valid @NotNull @PathVariable("id") String id) {
        log.info("Get customer by id");
        return customerService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Retrieve list by query ")
    public ResponseEntity<Page<CustomerDto>> customerGetList(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset) {

        log.info("Retrieve customer's list by query");
        var pageRequest = PageRequest.of(offset / limit, limit);
        return ResponseEntity.ok(customerService.getAllByPage(pageRequest));
    }


}
