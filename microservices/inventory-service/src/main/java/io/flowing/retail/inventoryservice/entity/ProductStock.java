package io.flowing.retail.inventoryservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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