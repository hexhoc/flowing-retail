package io.flowing.retail.inventoryservice.service;

import java.util.List;
import java.util.Objects;
import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import io.flowing.retail.inventoryservice.dto.mapper.ProductStockMapper;
import io.flowing.retail.inventoryservice.entity.ProductStock;
import io.flowing.retail.inventoryservice.repository.ProductStockQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service to read stock
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ProductStockQueryService {

    private final ProductStockQueryRepository productStockQueryRepository;

    /**
     * Get all stock by criteria
     * @return List of products stock
     */
    public Page<ProductStockDTO> getAll(Integer productId, Integer warehouseId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        // TODO: Use JpaRepository criteria instead
        Page<ProductStock> entityPage;
        if (Objects.nonNull(productId) && Objects.nonNull(warehouseId)){
            entityPage = productStockQueryRepository.findAllByProductIdAndWarehouseId(productId, warehouseId, pageRequest);
        } else if (Objects.nonNull(productId)) {
            entityPage = productStockQueryRepository.findAllByProductId(productId, pageRequest);
        } else if (Objects.nonNull(warehouseId)) {
            entityPage = productStockQueryRepository.findAllByWarehouseId(warehouseId, pageRequest);
        } else {
            entityPage = productStockQueryRepository.findAll(pageRequest);
        }

        List<ProductStockDTO> dtoList = entityPage.stream()
                .map(ProductStockMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

}
