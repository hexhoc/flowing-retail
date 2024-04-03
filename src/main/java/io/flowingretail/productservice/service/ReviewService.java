package io.flowingretail.productservice.service;

import io.flowingretail.productservice.dto.ReviewDTO;
import io.flowingretail.productservice.dto.mapper.ReviewMapper;
import io.flowingretail.productservice.entity.Review;
import io.flowingretail.productservice.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Integer save(ReviewDTO dto) {
        Review entity = ReviewMapper.toEntity(dto);
        entity = reviewRepository.save(entity);
        return entity.getId();
    }

    public void delete(Integer id) {
        reviewRepository.deleteById(id);
    }

    public void update(Integer id, ReviewDTO dto) {
        Review entity = requireOne(id);
        Review updatedEntity = ReviewMapper.toEntity(dto);
        BeanUtils.copyProperties(updatedEntity, entity);
        reviewRepository.save(entity);
    }

    public ReviewDTO getById(Integer id) {
        Review entity = requireOne(id);
        return ReviewMapper.toDto(entity);
    }

    public Page<ReviewDTO> getAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Review> entityPage = reviewRepository.findAll(pageRequest);
        List<ReviewDTO> dtoList = entityPage.stream()
                .map(ReviewMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList);
    }

    private Review requireOne(Integer id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }
}