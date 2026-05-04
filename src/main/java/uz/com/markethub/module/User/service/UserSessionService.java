package uz.com.markethub.module.User.service;

import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.security.dto.JWTTokenDTO;

public interface UserSessionService {

    void create(Long logId, JWTTokenDTO jwtTokenDTO, UserEntity user);
}
