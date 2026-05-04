package uz.com.markethub.module.User.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.domain.enums.RoleType;
import uz.com.markethub.module.User.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(Long logId, UserDTO.CreateOrUpdate entity);
    List<UserDTO.Full> findAll(Long logId);
    Page<UserDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable);
    Page<UserDTO> findAllByRoleTypes(Long logId, List<RoleType> roleTypes, Pageable pageable);
    List<UserDTO> findAllByRoleTypes(Long logId, List<RoleType> roleTypes);
    UserEntity findEntityById(Long logId, Long id);
    UserEntity findEntityByTelegramChatId(Long logId, Long id);
    UserDTO.Full findById(Long logId, Long id);
    UserDTO update(Long logId, Long id, UserDTO.CreateOrUpdate dto);
    void deleteById(Long logId, Long id);
    boolean existsById(Long logId, Long id);
}
