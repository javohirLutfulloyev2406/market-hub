package uz.com.markethub.module.User.repository;

import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.User.domain.UserSession;


@Repository
public interface UserSessionRepository extends GenericRepository<UserSession, Long> {
}
