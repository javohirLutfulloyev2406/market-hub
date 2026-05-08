package uz.com.markethub.module.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.exception.ResourceNotFoundException;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.specification.GenericSpecifications;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.domain.enums.RoleType;
import uz.com.markethub.module.User.dto.UserDTO;
import uz.com.markethub.module.User.repository.UserRepository;
import uz.com.markethub.module.User.service.RoleService;
import uz.com.markethub.module.User.service.UserService;

import java.util.HashSet;
import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public UserDTO create(Long logId, UserDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, create data: {}", logId, dto);

        UserEntity savedUser = repository.save(dto.map2Entity(
                new HashSet<>(roleService.findEntitiesByIds(logId, dto.getRoleIds())),
                passwordEncoder.encode(dto.getPassword())
        ));

        log.debug("Response -- logId: {}, data: {}", logId, savedUser);
        return savedUser.map2FullDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO.Full> findAll(Long logId) {
        log.debug("Request -- logId: {}, findAll", logId);

        List<UserDTO.Full> dtoList = repository
                .findAll()
                .stream()
                .map(UserEntity::map2FullDTO)
                .toList();
        log.debug("Response -- logId: {},data: {}", logId, dtoList);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO.Full> findAllPaged(Long logId, MultiValueMap<String, String> filters, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy pageable: {}", logId, pageable);

        Page<UserDTO.Full> pages = repository.findAll(
                        GenericSpecifications.byFilters(filters.toSingleValueMap()),
                        pageable)
                .map(UserEntity::map2FullDTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllByRoleTypes(Long logId, List<RoleType> roleTypes, Pageable pageable) {
        log.debug("Request -- logId: {}, findAllBy roleTypes: {}", logId, roleTypes);

        Page<UserDTO> pages = repository.findDistinctByRolesRoleTypeIn(roleTypes,
                        pageable)
                .map(UserEntity::map2DTO);

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllByRoleTypes(Long logId, List<RoleType> roleTypes) {
        log.debug("Request -- logId: {}, findAllBy roleTypes: {}", logId, roleTypes);

        List<UserDTO> pages = repository.findDistinctByRolesRoleTypeIn(roleTypes)
                .stream()
                .map(UserEntity::map2DTO)
                .toList();

        log.debug("Response -- logId: {}, data: {}", logId, pages);
        return pages;
    }


    @Override
    public UserEntity findEntityById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findByEntityBy id: {}", logId, id);

        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {},data: {}", logId, entity);
        return entity;

    }
    @Override
    @Transactional(readOnly = true)
    public UserEntity findEntityByTelegramChatId(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityByTelegramChatId id: {}", logId, id);

        UserEntity entity = repository.findByTelegramChatId(String.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {},data: {}", logId, entity);
        return entity;

    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO.Full findById(Long logId, Long id) {
        log.debug("Request -- logId: {}, findEntityBy id: {}", logId, id);

        UserDTO.Full dto = repository
                .findById(id)
                .map(UserEntity::map2FullDTO)
                .orElseThrow(() -> new ResourceNotFoundException(logId, id, ApiStatus.ERR_ID_NOT_FOUND));

        log.debug("Response -- logId: {}, data: {}", logId, dto);
        return dto;
    }

    @Override
    @Transactional
    public UserDTO update(Long logId, Long id, UserDTO.CreateOrUpdate dto) {
        log.debug("Request -- logId: {}, update data: {}", logId, dto);

        UserEntity entity = findEntityById(logId, id);

        dto.set2Entity(entity);

        UserEntity savedEntity = repository.save(entity);

        log.debug("Response -- logId: {}, data: {}", logId, savedEntity);
        return savedEntity.map2FullDTO();
    }

    @Override
    public void deleteById(Long logId, Long id) {
        log.debug("Request -- logId: {}, deleteBy id: {}", logId, id);

        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long logId, Long id) {
        log.debug("Request -- logId: {}, existBy id: {}", logId, id);

        boolean exists = repository.existsById(id);

        log.debug("Response -- logId: {}, data: {}", logId, exists);
        return exists;
    }
}
