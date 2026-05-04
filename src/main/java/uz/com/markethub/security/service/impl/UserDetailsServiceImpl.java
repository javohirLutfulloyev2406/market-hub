package uz.com.markethub.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.repository.UserRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String username) {
        log.debug("Request -- findBy  username: {}", username);

        return userRepository.findOneByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found by username: " + username));
    }
}
