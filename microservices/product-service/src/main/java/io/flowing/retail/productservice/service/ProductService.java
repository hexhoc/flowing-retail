package io.flowing.retail.productservice.service;

import io.flowing.retail.productservice.dto.ProductDTO;
import io.flowing.retail.productservice.entity.Product;
import io.flowing.retail.productservice.dto.mapper.ProductMapper;
import io.flowing.retail.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Integer save(ProductDTO dto) {
        Product entity = ProductMapper.toEntity(dto);
        entity = productRepository.save(entity);
        return entity.getId();
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public void update(Integer id, ProductDTO dto) {
        Product entity = requireOne(id);
        Product updatedEntity = ProductMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity,"id", "version","createdDate","modifiedDate");
        productRepository.save(entity);
    }

    public ProductDTO getById(Integer id) {
        Product entity = requireOne(id);
        return ProductMapper.toDto(entity);
    }

    public Page<ProductDTO> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Product> entityPage = productRepository.findAll(pageRequest);
        List<ProductDTO> dtoList = entityPage.stream()
                .map(ProductMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    private Product requireOne(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
