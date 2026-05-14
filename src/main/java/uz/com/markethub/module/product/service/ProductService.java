package uz.com.markethub.module.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.product.domain.ProductEntity;
import uz.com.markethub.module.product.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO.Full create(Long logId, ProductDTO.CreateOrUpdate dto);

    ProductDTO.Full update(Long logId, Long id, ProductDTO.CreateOrUpdate dto);

    Page<ProductDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<ProductDTO> findAllShortInfo(Long logId);

    ProductDTO.Full findById(Long logId, Long id);

    ProductEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    void delete(Long logId, ProductEntity entity);

    boolean existsById(Long logId, Long id);

    void decreaseStock(Long logId, ProductEntity product, Integer quantity);
}