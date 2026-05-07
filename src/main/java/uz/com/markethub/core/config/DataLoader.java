package uz.com.markethub.core.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.com.markethub.module.User.domain.ApiPartnerEntity;
import uz.com.markethub.module.User.domain.PermissionEntity;
import uz.com.markethub.module.User.domain.RoleEntity;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.domain.enums.PermissionAction;
import uz.com.markethub.module.User.repository.ApiPartnerRepository;
import uz.com.markethub.module.User.repository.PermissionRepository;
import uz.com.markethub.module.User.repository.RoleRepository;
import uz.com.markethub.module.User.repository.UserRepository;
import uz.com.markethub.module.User.util.ApiPartnerUtil;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${init.database}")
    private boolean initDatabase;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ApiPartnerRepository apiPartnerRepository;
    @Override
    @Transactional
    public void run(String... args) {
        if (!initDatabase) {
            log.info(" init.database=false, skipping DataLoader...");
            return;
        }

        log.info("⚙️ DataLoader started");

        createPermissionsIfNotExists();

        RoleEntity superAdminRole = createRoleIfNotExists(
                "Super admin",
                "Супер админ",
                "Супер админ",
                "All permissions for full access"
        );

        UserEntity superAdmin = createUserWithRole(
                "Super Admin",
                "superadmin",
                "admin123",
                BigDecimal.ZERO,
                superAdminRole
        );

        createUserWithRole(
                "Javohir Lutfullayev",
                "javohir_jasurovich",
                "123456",
                new BigDecimal("100000.00"),
                superAdminRole
        );

        createUserWithRole(
                "Murtazo Lutfullayev",
                "murtazo_N",
                "123456",
                new BigDecimal("150000.00"),
                superAdminRole
        );

        createApiPartnerIfNotExists("Android");
        createApiPartnerIfNotExists("iOS");

        log.info(" DataLoader finished");
    }

    private void createPermissionsIfNotExists() {
        for (PermissionAction action : PermissionAction.values()) {
            String code = "PERMISSION_" + action.name();


            PermissionEntity permission = PermissionEntity.builder()
                    .code(code)
                    .action(action)
                    .description("Auto-generated permission for: " + action.name())
                    .build();
            permissionRepository.save(permission);
            log.info("✅ Created permission: {}", code);
        }
    }

    private RoleEntity createRoleIfNotExists(String nameUz, String nameEn, String nameRu, String description) {
        Optional<RoleEntity> optional = roleRepository.findAll()
                .stream()
                .filter(r -> nameUz.equalsIgnoreCase(r.getNameUz()))
                .findFirst();

        if (optional.isPresent()) {
            log.info("️ Role already exists: {}", nameUz);
            return optional.get();
        }

        Set<PermissionEntity> permissions = new LinkedHashSet<>(permissionRepository.findAll());

        RoleEntity role = RoleEntity.builder()
                .nameUz(nameUz)
                .nameEn(nameEn)
                .nameRu(nameRu)
                .description(description)
                .permissions(permissions)
                .build();

        RoleEntity saved = roleRepository.save(role);
        log.info(" Role created: {}", nameUz);
        return saved;
    }

    private UserEntity createUserWithRole(String name, String username, String rawPassword, BigDecimal balance, RoleEntity role) {
        Optional<UserEntity> optional = userRepository.findByUsername(username);
        if (optional.isPresent()) {
            log.info("ℹ User already exists: {}", username);
            return optional.get();
        }

        Set<RoleEntity> roles = new LinkedHashSet<>();
        roles.add(role);

        UserEntity user = UserEntity.builder()
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .roles(roles)
                .build();

        UserEntity saved = userRepository.save(user);
        log.info("User created: {}", username);
        return saved;
    }

    private void createApiPartnerIfNotExists(String name) {
        if (apiPartnerRepository.existsByName(name)) {
            log.info(" ApiPartner already exists: {}", name);
            return;
        }

        ApiPartnerEntity partner = ApiPartnerEntity.builder()
                .name(name)
                .code(ApiPartnerUtil.generateFormattedKey())
                .enabled(true)
                .version("v1.0")
                .description(name + " client integration")
                .build();

        apiPartnerRepository.save(partner);
        log.info("✅ ApiPartner created: {}", name);
    }
}
