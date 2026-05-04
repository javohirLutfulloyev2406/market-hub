package uz.com.markethub.module.User.service;

import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.User.domain.PermissionEntity;
import uz.com.markethub.module.User.dto.PermissionDTO;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    PermissionDTO.Full create(Long logId, PermissionDTO dto);

    Page<PermissionDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<PermissionDTO> findAllShortInfo(Long logId, MultiValueMap<String, String> filters);

    PermissionDTO.Full findById(Long logId, Long id);

    PermissionDTO.Full update(Long logId, Long id, PermissionDTO.Full dto);

    PermissionEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    void delete(Long logId, PermissionEntity entity);

    boolean existsById(Long logId, Long id);

    Set<PermissionEntity> findAllByPermissionIds(Long logId, List<Long> permissionIds);

}
