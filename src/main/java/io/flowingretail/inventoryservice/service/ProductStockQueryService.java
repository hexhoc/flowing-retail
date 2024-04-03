package io.flowingretail.inventoryservice.service;

import io.flowingretail.inventoryservice.dto.ProductStockDto;
import io.flowingretail.inventoryservice.dto.mapper.ProductStockMapper;
import io.flowingretail.inventoryservice.entity.ProductStock;
import io.flowingretail.inventoryservice.repository.ProductStockQueryRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductStockQueryService {

    private final ProductStockQueryRepository productStockQueryRepository;

    /**
     * Get all stock by criteria
     * @return List of products stock
     */
    public Page<ProductStockDto> getAll(Integer productId, Integer warehouseId, Integer page, Integer size) {
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

        List<ProductStockDto> dtoList = entityPage.stream()
                .map(ProductStockMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

}
