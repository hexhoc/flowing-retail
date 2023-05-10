package io.flowing.retail.inventoryservice.service;

import io.flowing.retail.inventoryservice.domain.PickOrder;
import io.flowing.retail.inventoryservice.dto.InventoryItemDTO;
import io.flowing.retail.inventoryservice.dto.ProductStockDTO;
import io.flowing.retail.inventoryservice.dto.mapper.ProductStockMapper;
import io.flowing.retail.inventoryservice.entity.ProductStock;
import io.flowing.retail.inventoryservice.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    /**
     * reserve goods on stock for a defined period of time
     *
     * @param reason A reason why the goods are reserved (e.g. "customer order")
     * @param refId A reference id fitting to the reason of reservation (e.g. the order id), needed to find reservation again later
     * @param expirationDate Date until when the goods are reserved, afterwards the reservation is removed
     * @return if reservation could be done successfully
     */
    public boolean reserveGoods(List<InventoryItemDTO> items, String reason, String refId, LocalDateTime expirationDate) {
        // TODO: Implement
        return true;
    }

    /**
     * Order to pick the given items in the warehouse. The inventory is decreased.
     * Reservation fitting the reason/refId might be used to fulfill the order.
     *
     * If no enough items are on stock - an exception is thrown.
     * Otherwise a unique pick id is returned, which can be used to
     * reference the bunch of goods in the shipping area.
     *
     * @param items to be picked
     * @param reason for which items are picked (e.g. "customer order")
     * @param refId Reference id fitting to the reason of the pick (e.g. "order id"). Used to determine which reservations can be used.
     * @return a unique pick ID
     */
    public String pickItems(Set<InventoryItemDTO> items, String reason, String refId) {

        List<Integer> productIds = items.stream().map(InventoryItemDTO::getProductId).toList();
        List<ProductStock> productStockList = productStockRepository.findAllForPickUp(productIds, 1);
        Map<Integer, ProductStock> productStockMap = productStockList.stream()
                .collect(Collectors.toMap(p -> p.getProductStockId().getProductId(), p -> p));

        for (InventoryItemDTO item : items) {
            // TODO: сделать возможность отказа из-за нехватки товара. 90% что товар есть, 10% может отсутствовать
            var productStock = productStockMap.get(item.getProductId());
            productStock.setStock(productStock.getStock() - item.getQuantity());
        }

        productStockRepository.saveAll(productStockList);

        // Long operation
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PickOrder pickOrder = new PickOrder(items);
        log.info("# Items picked: " + pickOrder);

        return pickOrder.getId();
    }

    /**
     * New goods are arrived and inventory is increased
     */
    public void topUpInventory(String articleId, int amount) {
        // TODO: Implement
    }

    public void batchDelete(List<ProductStockDTO> dtoList) {
        var ids = dtoList.stream()
                .map(dto -> ProductStockMapper.toEntity(dto).getProductStockId())
                .toList();
        productStockRepository.deleteAllByIdInBatch(ids);
    }

    public void batchUpdate(List<ProductStockDTO> dtoList) {
        var productStockList = dtoList.stream()
                .map(ProductStockMapper::toEntity)
                .toList();
        productStockRepository.saveAll(productStockList);
    }

    public Page<ProductStockDTO> getAll(Integer productId, Integer warehouseId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        // TODO: Use JpaRepository criteria instead
        Page<ProductStock> entityPage;
        if (Objects.nonNull(productId) && Objects.nonNull(warehouseId)){
            entityPage = productStockRepository.findAllByProductIdAndWarehouseId(productId, warehouseId, pageRequest);
        } else if (Objects.nonNull(productId)) {
            entityPage = productStockRepository.findAllByProductId(productId, pageRequest);
        } else if (Objects.nonNull(warehouseId)) {
            entityPage = productStockRepository.findAllByWarehouseId(warehouseId, pageRequest);
        } else {
            entityPage = productStockRepository.findAll(pageRequest);
        }

        List<ProductStockDTO> dtoList = entityPage.stream()
                .map(ProductStockMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

}
