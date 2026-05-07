package uz.com.markethub.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uz.com.markethub.core.repository.impl.GenericRepositoryImpl;

@Configuration
@EnableJpaRepositories(
        basePackages = "uz.com.markethub",
        repositoryBaseClass = GenericRepositoryImpl.class
)
public class JpaConfig {
    
}

