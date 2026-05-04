package uz.com.markethub.module.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.domain.UserSession;
import uz.com.markethub.module.User.repository.UserSessionRepository;
import uz.com.markethub.module.User.service.UserSessionService;
import uz.com.markethub.security.dto.JWTTokenDTO;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {
    private final UserSessionRepository repository;


    @Override
    public void create(Long logId, JWTTokenDTO jwtTokenDTO, UserEntity user) {
        log.debug("Request -- logId: {}, create data: {}", logId, jwtTokenDTO);

        UserSession
                .builder()
                .accessToken(jwtTokenDTO.getAccessToken())
                .expireDate(LocalDateTime.now())
                .active(true)
                .build();

//        repository.save(entity);

        log.debug("Response -- logId: {}, data: {}", logId, null);
    }
}
