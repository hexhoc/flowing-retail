package io.flowingretail.paymentservice.adapter.in.http;

import io.flowingretail.paymentservice.dto.PaymentDto;
import io.flowingretail.paymentservice.service.PaymentService;
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
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(
        name = "Payment",
        description = "Operations with payments"
)
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<PaymentDto>> getList(@RequestParam(name = "orderId", required = false) Integer orderId,
                                                    @RequestParam(name = "customerId", required = false) Integer customerId,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(paymentService.getAll(orderId, customerId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a list")
    public ResponseEntity<PaymentDto> getById(@Valid @NotNull @PathVariable(name = "id") Integer id) {
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
                       @Valid @RequestBody PaymentDto dto) {
        paymentService.update(id, dto);
    }

}
