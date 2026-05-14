package uz.com.markethub.module.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.exception.InvalidParameterException;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.category.service.CategoryService;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.product.dto.ProductDTO;
import uz.com.markethub.module.product.repository.ProductRepository;
import uz.com.markethub.module.product.service.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public ProductDTO.Full create(Long logId, ProductDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        if (Objects.nonNull(dto.getSku()) && repository.existsBySkuAndDeletedFalse(dto.getSku())) {
            throw new InvalidParameterException(logId, dto.getSku(), ApiStatus.ERR_DUPLICATE_VALUE);
        }

        ProductDTO.Full fullDTO = repository
                .save(dto.map2Entity(categoryService.findEntityById(logId, dto.getCategoryId())))
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional
    public ProductDTO.Full update(Long logId, Long id, ProductDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, update data: {}", logId, dto);

        ProductEntity entity = findEntityById(logId, id);

        if (Objects.nonNull(dto.getSku()) && repository.existsBySkuAndIdNotAndDeletedFalse(dto.getSku(), id)) {
            throw new InvalidParameterException(logId, dto.getSku(), ApiStatus.ERR_DUPLICATE_VALUE);
        }

        dto.set2Entity(entity, categoryService.findEntityById(logId, dto.getCategoryId()));

        ProductDTO.Full fullDTO = repository
                .save(entity)
                .map2FullDTO();

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<ProductDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(ProductEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllShortInfo(Long logId) {
        log.debug("Request -- logId: {}, findAllShortInfo", logId);

        List<ProductDTO> result = repository.findAll()
                .stream()
                .map(ProductEntity::map2DTO)
                .collect(Collectors.toList());

        log.debug("Response -- logId: {}, data: {}", logId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findBy id: {}", logId, id);

        ProductDTO.Full fullDTO = repository
                .findById(id)
                .map(ProductEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, fullDTO);
        return fullDTO;
    }

    @Override
    public ProductEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        ProductEntity entity = findEntityById(logId, id);
        delete(logId, entity);
    }

    @Override
    @Transactional
    public void delete(Long logId, ProductEntity entity) {
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

    @Override
    @Transactional
    public void decreaseStock(Long logId, ProductEntity product, Integer quantity) {
        log.debug("Request -- logId: {}, decreaseStock product: {}, quantity: {}", logId, product.getId(), quantity);

        if (product.getQuantity() < quantity) {
            throw new InvalidParameterException(logId, product.getSku(), ApiStatus.ERR_INSUFFICIENT_STOCK);
        }
        product.setQuantity(product.getQuantity() - quantity);
        repository.save(product);

        log.debug("Response -- logId: {}, remaining stock: {}", logId, product.getQuantity());
    }
}