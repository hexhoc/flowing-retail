package io.flowingretail.productservice.service;

import io.flowingretail.productservice.dto.ProductImageDTO;
import io.flowingretail.productservice.dto.mapper.ProductImageMapper;
import io.flowingretail.productservice.entity.ProductImage;
import io.flowingretail.productservice.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public List<ProductImageDTO> getAllByProductId(Integer productId) {
        List<ProductImage> imagesList = productImageRepository.findAllByProductId(productId);
        return imagesList.stream().map(ProductImageMapper::toDto).toList();
    }

    public void uploadImageToProduct(MultipartFile file, int productId) throws IOException {
        log.info(String.format("Uploading image to product id %s", productId));

        ProductImage productImage = new ProductImage();
        productImage.setName(file.getOriginalFilename());
        productImage.setProductId(productId);
        productImage.setImageBytes(compressBytes(file.getBytes()));

        productImageRepository.save(productImage);
    }

    @Transactional
    public void deleteByNameAndProductId(String imageName, Integer productId) {
        productImageRepository.deleteByNameAndProductId(imageName, productId);
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

}
