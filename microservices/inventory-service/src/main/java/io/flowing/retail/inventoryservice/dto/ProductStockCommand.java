package io.flowing.retail.inventoryservice.dto;

import javax.validation.constraints.Size;

import lombok.Data;

import java.util.List;

@Data
public class ProductStockCommand {
    @Size(min=1, max=100, message = "лимит пакета 100 записей на обновление")
    private List<ProductStockDTO> stocks;
}
