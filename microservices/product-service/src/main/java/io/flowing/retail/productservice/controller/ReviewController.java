package io.flowing.retail.productservice.controller;

import io.flowing.retail.productservice.dto.ReviewDTO;
import io.flowing.retail.productservice.service.ReviewService;
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
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(
        name = "Review",
        description = "Operations with product review"
)
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get a list")
    public ResponseEntity<Page<ReviewDTO>> getList(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(reviewService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the review by id")
    public ResponseEntity<ReviewDTO> getById(@Valid @NotNull @PathVariable("id") Integer id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new review")
    public ResponseEntity<String> save(@Valid @RequestBody ReviewDTO dto) {
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
                       @Valid @RequestBody ReviewDTO dto) {
        reviewService.update(id, dto);
    }


}
