package io.flowing.retail.shippingservice.service;
import io.flowing.retail.shippingservice.dto.WaybillDTO;
import io.flowing.retail.shippingservice.dto.mapper.WaybillMapper;
import io.flowing.retail.shippingservice.entity.Waybill;
import io.flowing.retail.shippingservice.repository.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;

    /**
     *
     * @param pickId - required to identify the pile of goods to be packed in the parcel
     * @param recipientName - name of recipient
     * @param recipientAddress complete address the shipment is sent to
     * @param logisticsProvider delivering the shipment (e.g. DHL, UPS, ...)
     * @return shipment id created (also printed on the label of the parcel)
     */
    public String createShipment(String orderId, String pickId, String recipientName, String recipientAddress, String logisticsProvider) {
        System.out.println("Shipping to " + recipientName + "\n\n" + recipientAddress);
        Waybill w = new Waybill();
        w.setWaybillDate(LocalDateTime.now());
        w.setOrderId(orderId);
        w.setRecipientName(recipientName);
        w.setRecipientAddress(recipientAddress);
        w.setLogisticsProvider(logisticsProvider);

        shippingRepository.save(w);

        return w.getId().toString();
    }

    public void delete(Integer id) {
        shippingRepository.deleteById(id);
    }

    public void update(Integer id, WaybillDTO dto) {
        Waybill entity = requireOne(id);
        Waybill updatedEntity = WaybillMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity, "id");
        shippingRepository.save(entity);
    }

    public Page<WaybillDTO> getAll(Integer orderId, Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);

        Page<Waybill> entityPage;
        if (Objects.nonNull(orderId)){
            entityPage = shippingRepository.findAllByOrderId(orderId, pageRequest);
        } else {
            entityPage = shippingRepository.findAll(pageRequest);
        }

        List<WaybillDTO> dtoList = entityPage.stream()
                .map(WaybillMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    public WaybillDTO getById(Integer id) {
        return WaybillMapper.toDto(requireOne(id));
    }

    private Waybill requireOne(Integer id) {
        return shippingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
