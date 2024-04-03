package io.flowingretail.productservice.service;

import io.flowingretail.productservice.dto.ProductDto;
import io.flowingretail.productservice.dto.mapper.ProductMapper;
import io.flowingretail.productservice.entity.Product;
import io.flowingretail.productservice.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Integer save(ProductDto dto) {
        Product entity = ProductMapper.toEntity(dto);
        entity = productRepository.save(entity);
        return entity.getId();
    }

    public void saveBatch(List<ProductDto> dtoList) {
        List<Product> entityList = dtoList.stream().map(ProductMapper::toEntity).toList();
        productRepository.saveAll(entityList);
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public void update(Integer id, ProductDto dto) {
        Product entity = requireOne(id);
        Product updatedEntity = ProductMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity,"id", "version","createdDate","modifiedDate");
        productRepository.save(entity);
    }

    public ProductDto getById(Integer id) {
        Product entity = requireOne(id);
        return ProductMapper.toDto(entity);
    }

    public Page<ProductDto> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Product> entityPage = productRepository.findAll(pageRequest);
        List<ProductDto> dtoList = entityPage.stream()
                .map(ProductMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    private Product requireOne(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}
