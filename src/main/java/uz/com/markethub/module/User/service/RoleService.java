package uz.com.markethub.module.User.service;

import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.com.markethub.module.User.domain.RoleEntity;
import uz.com.markethub.module.User.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO.Full create(Long logId, RoleDTO.CreateOrUpdate dto);

    RoleDTO.Full update(Long logId, Long id, RoleDTO.CreateOrUpdate dto);

    Page<RoleDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);

    List<RoleDTO> findAllShortInfo(Long logId);

    RoleDTO.Full findById(Long logId, Long id);

    RoleEntity findEntityById(Long logId, Long id);

    void deleteById(Long logId, Long id);

    void delete(Long logId, RoleEntity entity);

    boolean existsById(Long logId, Long id);

    List<RoleEntity> findEntitiesByIds(Long logId, List<Long> ids);
}
