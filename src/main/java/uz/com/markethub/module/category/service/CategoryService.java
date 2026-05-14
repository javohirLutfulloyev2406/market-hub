package uz.com.markethub.module.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.category.domain.CategoryEntity;
import uz.com.markethub.module.category.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO.Full create(Long logId, CategoryDTO.CreateOrUpdate dto);

    CategoryDTO.Full update(Long logId, Long id, CategoryDTO.CreateOrUpdate dto);

    Page<CategoryDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<CategoryDTO> findAllShortInfo(Long logId);

    CategoryDTO.Full findById(Long logId, Long id);

    CategoryEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    void delete(Long logId, CategoryEntity entity);

    boolean existsById(Long logId, Long id);
}