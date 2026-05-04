package uz.com.markethub.security.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.service.ApiPartnerService;
import uz.com.markethub.module.User.service.UserSessionService;
import uz.com.markethub.security.dto.AccessTokenDTO;
import uz.com.markethub.security.execption.ApiPartnerException;
import uz.com.markethub.security.execption.UserNotAuthorizedException;
import uz.com.markethub.security.jwt.TokenProvider;
import uz.com.markethub.security.service.AuthService;
import uz.com.markethub.security.vm.LoginVM;


@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("#{new Long('${app.jwt.expire}')}")
    private Long jwtExpire;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ApiPartnerService apiPartnerService;
    private final UserSessionService userSessionService;

    @Override
    @Transactional
    public AccessTokenDTO authenticate(Long logId, LoginVM vm, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(vm.getUsername(), vm.getPassword());

            var authentication = authenticationManagerBuilder
                    .getObject()
                    .authenticate(authenticationToken);

            UserEntity user = (UserEntity) authentication.getPrincipal();

//            PartnerInfoRecord apiPartnerInfo = HelperRequestUtil.getApiPartnerInfo(request);
//
//            ApiPartnerDTO.Full apiPartnerFullDTO = apiPartnerService.findByCode(apiPartnerInfo.code());
//
//            apiPartnerService.checkIsEnabledAndCorrectVersion(
//                    apiPartnerFullDTO.isEnabled(), apiPartnerFullDTO.getVersion(), apiPartnerInfo.version()
//            );

            String token = tokenProvider.generateSystemAccessToken(user.getUsername());

            return AccessTokenDTO.builder()
                    .accessToken(token)
                    .build();

        } catch (Exception e) {
            if (e instanceof ApiPartnerException exception) {
                throw exception;
            } else {
                throw new UserNotAuthorizedException(String.valueOf(e.getMessage()));
            }
        }
    }
}
