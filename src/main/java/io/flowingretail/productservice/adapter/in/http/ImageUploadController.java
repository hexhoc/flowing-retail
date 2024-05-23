package io.flowingretail.productservice.adapter.in.http;

import io.flowingretail.productservice.dto.ProductImageDto;
import io.flowingretail.productservice.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ProductImageService productImageService;


    @GetMapping("/{productId}")
    @Operation(summary = "Get list of images for specific product id")
    public ResponseEntity<List<ProductImageDto>> getImageToPost(@PathVariable("productId") Integer productId) {
        List<ProductImageDto> imagesList = productImageService.getAllByProductId(productId);
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