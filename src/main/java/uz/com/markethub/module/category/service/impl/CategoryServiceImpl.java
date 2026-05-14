package uz.com.markethub.module.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.category.domain.CategoryEntity;
import uz.com.markethub.module.category.dto.CategoryDTO;
import uz.com.markethub.module.category.repository.CategoryRepository;
import uz.com.markethub.module.category.service.CategoryService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'tree'")
    public CategoryDTO.Full create(Long logId, CategoryDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        CategoryEntity parentCategory = Objects.nonNull(dto.getParentId())
                ? findEntityById(logId, dto.getParentId())
                : null;

        CategoryDTO.Full fullDTO = repository
                .save(dto.map2Entity(parentCategory))
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'tree'")
    public CategoryDTO.Full update(Long logId, Long id, CategoryDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, update data: {}", logId, dto);

        CategoryEntity entity = findEntityById(logId, id);

        CategoryEntity parentCategory = Objects.nonNull(dto.getParentId())
                ? findEntityById(logId, dto.getParentId())
                : null;

        dto.set2Entity(entity, parentCategory);

        CategoryDTO.Full fullDTO = repository
                .save(entity)
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<CategoryDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(CategoryEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "'tree'")
    public List<CategoryDTO> findAllShortInfo(Long logId) {
        log.debug("Request -- logId: {}, findAllShortInfo", logId);

        List<CategoryDTO> result = repository.findAll()
                .stream()
                .map(CategoryEntity::map2DTO)
                .collect(Collectors.toList());

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findBy id: {}", logId, id);

        CategoryDTO.Full fullDTO = repository
                .findById(id)
                .map(CategoryEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public CategoryEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        CategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'tree'")
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        CategoryEntity entity = findEntityById(logId, id);
        delete(logId, entity);
    }

    @Override
    @Transactional
    public void delete(Long logId, CategoryEntity entity) {
        log.debug("Request -- logId: {}, delete data: {}", logId, entity);

        entity.setDeleted(true);
        repository.save(entity);
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existsBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.debug("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }
}