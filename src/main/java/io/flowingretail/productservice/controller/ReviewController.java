package io.flowingretail.productservice.controller;

import io.flowingretail.productservice.dto.ReviewDto;
import io.flowingretail.productservice.service.ReviewService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(
        name = "Review",
        description = "Operations with product review"
)
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<ReviewDto>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(reviewService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the review by id")
    public ResponseEntity<ReviewDto> getById(@Valid @NotNull @PathVariable("id") Integer id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new review")
    public ResponseEntity<String> save(@Valid @RequestBody ReviewDto dto) {
        return ResponseEntity.ok(reviewService.save(dto).toString());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review by id")
    public void delete(@Valid @NotNull @PathVariable("id") Integer id) {
        reviewService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing review by id")
    public void update(@Valid @NotNull @PathVariable("id") Integer id,
                       @Valid @RequestBody ReviewDto dto) {
        reviewService.update(id, dto);
    }


}
