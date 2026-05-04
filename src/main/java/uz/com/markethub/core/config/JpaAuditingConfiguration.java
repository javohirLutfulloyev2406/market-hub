package uz.com.markethub.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.com.markethub.security.util.SecurityUtils;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public  Optional<String> getCurrentAuditor() {
            var currentUser = SecurityUtils.getCurrentUserLogin();
            if (currentUser.isPresent()) {
                return currentUser;
            } else {
                return Optional.of("system");
            }
        }
    }

}
