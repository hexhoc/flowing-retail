package io.flowing.retail.shippingservice.controller;

import io.flowing.retail.shippingservice.dto.WaybillDTO;
import io.flowing.retail.shippingservice.service.ShippingService;
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
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
@Tag(
        name = "Shipping",
        description = "Operations with payments"
)
@Log4j2
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<WaybillDTO>> getList(@RequestParam(name = "orderId", required = false) Integer orderId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(shippingService.getAll(orderId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id")
    public ResponseEntity<WaybillDTO> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
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
                       @Valid @RequestBody WaybillDTO dto) {
        shippingService.update(id, dto);
    }

}
