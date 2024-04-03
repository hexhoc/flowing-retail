package io.flowingretail.shippingservice.controller;

import io.flowingretail.shippingservice.dto.WaybillDto;
import io.flowingretail.shippingservice.service.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
@Tag(
        name = "Shipping",
        description = "Operations with payments"
)
@Slf4j
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<WaybillDto>> getList(@RequestParam(name = "orderId", required = false) Integer orderId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(shippingService.getAll(orderId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id")
    public ResponseEntity<WaybillDto> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(shippingService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete by id")
    public void delete(@Valid @NotNull @PathVariable("id") Integer id) {
        shippingService.delete(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public void update(@Valid @NotNull @PathVariable("id") Integer id,
                       @Valid @RequestBody WaybillDto dto) {
        shippingService.update(id, dto);
    }

}
