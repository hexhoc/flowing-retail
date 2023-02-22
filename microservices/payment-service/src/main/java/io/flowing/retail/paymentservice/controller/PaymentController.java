package io.flowing.retail.paymentservice.controller;

import io.flowing.retail.paymentservice.dto.PaymentDTO;
import io.flowing.retail.paymentservice.service.PaymentService;
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
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(
        name = "Payment",
        description = "Operations with payments"
)
@Log4j2
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<PaymentDTO>> getList(@RequestParam(name = "orderId", required = false) Integer orderId,
                                                    @RequestParam(name = "customerId", required = false) Integer customerId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(paymentService.getAll(orderId, customerId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a list")
    public ResponseEntity<PaymentDTO> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete by id")
    public void delete(@Valid @NotNull @PathVariable("id") Integer id) {
        paymentService.delete(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update payments")
    public void update(@Valid @NotNull @PathVariable("id") Integer id,
                       @Valid @RequestBody PaymentDTO dto) {
        paymentService.update(id, dto);
    }

}
