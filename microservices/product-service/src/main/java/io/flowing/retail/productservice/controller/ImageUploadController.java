package io.flowing.retail.productservice.controller;

import io.flowing.retail.productservice.dto.ProductImageDTO;
import io.flowing.retail.productservice.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ProductImageService productImageService;


    @GetMapping("/{productId}")
    @Operation(summary = "Get list of images for specific product id")
    public ResponseEntity<List<ProductImageDTO>> getImageToPost(@PathVariable("productId") Integer productId) {
        List<ProductImageDTO> imagesList = productImageService.getAllByProductId(productId);
        return ResponseEntity.ok(imagesList);
    }

    @PostMapping("/{productId}/upload")
    @Operation(summary = "Upload 1 image to concrete product")
    public ResponseEntity<String> uploadImage(@PathVariable("productId") String productId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        productImageService.uploadImageToProduct(file, Integer.parseInt(productId));
        return ResponseEntity.ok("Image Uploaded Successfully");
    }

    @DeleteMapping("/{productId}/{imageName}")
    @Operation(summary = "Delete a specific image by its name and product ID")
    public void delete(@Valid @NotNull @PathVariable("imageName") String imageName,
                       @Valid @NotNull @PathVariable("productId") Integer productId) {
        productImageService.deleteByNameAndProductId(imageName, productId);
    }
}