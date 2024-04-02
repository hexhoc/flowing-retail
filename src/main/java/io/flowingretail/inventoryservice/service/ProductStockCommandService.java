package io.flowingretail.inventoryservice.service;

import io.flowingretail.common.dto.InventoryItemDTO;
import io.flowingretail.inventoryservice.dto.PickOrderDTO;
import io.flowingretail.inventoryservice.dto.ProductStockDTO;
import io.flowingretail.inventoryservice.dto.mapper.ProductStockMapper;
import io.flowingretail.inventoryservice.repository.ProductStockCommandRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service to write stock
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductStockCommandService {

    private final ProductStockCommandRepository productStockCommandRepository;

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

        var productIds = items.stream().map(InventoryItemDTO::getProductId).toList();
        var productStockList = productStockCommandRepository.findAllForPickUp(productIds, 1);
        var productStockMap = productStockList.stream()
                .collect(Collectors.toMap(p -> p.getProductStockId().getProductId(), p -> p));

        for (var item : items) {
            // TODO: сделать возможность отказа из-за нехватки товара. 90% что товар есть, 10% может отсутствовать
            var productStock = productStockMap.get(item.getProductId());
            productStock.setStock(productStock.getStock() - item.getQuantity());
        }

        productStockCommandRepository.saveAll(productStockList);

        // Long operation
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PickOrderDTO pickOrderDTO = new PickOrderDTO(items);
        log.info("# Items picked: " + pickOrderDTO);

        return pickOrderDTO.getId();
    }

    /**
     * Delete all record by id
     * @param dtoList list of products stock
     */
    public void batchDelete(List<ProductStockDTO> dtoList) {
        var ids = dtoList.stream()
                .map(dto -> ProductStockMapper.toEntity(dto).getProductStockId())
                .toList();
        productStockCommandRepository.deleteAllByIdInBatch(ids);
    }

    /**
     * Update already exist or add new if not found
     * @param dtoList list of products stock
     */
    public void batchUpdate(List<ProductStockDTO> dtoList) {
        var productStockList = dtoList.stream()
                .map(ProductStockMapper::toEntity)
                .toList();
        productStockCommandRepository.saveAll(productStockList);
    }

}
