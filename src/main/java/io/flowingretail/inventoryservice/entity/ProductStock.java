package io.flowingretail.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_stocks")
@Getter
@Setter
public class ProductStock {

    @EmbeddedId
    private ProductStockId productStockId;

    @Column(name = "stock")
    @NotNull
    private Integer stock;

    @Embeddable
    @Data
    public static class ProductStockId implements Serializable {
        @Column(name = "product_id")
        private Integer productId;

        @Column(name = "warehouse_id")
        private Integer warehouseId;
    }
}