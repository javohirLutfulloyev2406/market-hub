package uz.com.markethub.security.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.security.execption.UserNotAuthorizedException;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for Spring Security.
 */
public class SecurityUtils {

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    public static UserEntity getCurrentUserPrincipal() {
        Authentication authentication = getCurrentAuthentication();

        if (Objects.isNull(authentication)) {
            throw new UserNotAuthorizedException("Authentication is null");
        }

        return (UserEntity) authentication.getPrincipal();
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getAuthenticatedUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        return Objects.nonNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean hasAuthorityInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                getAuthoritiesStream(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthoritiesStream(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority);
    }

    public static Set<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public static boolean isAnonymous(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

    public static boolean isAnonymous() {
        return isAnonymous(getCurrentAuthentication());
    }

    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getAuthenticatedUserId() {
        Authentication authentication = getCurrentAuthentication();

        if (Objects.isNull(authentication)) {
            throw new UserNotAuthorizedException("Authentication is null");
        }

        return ((UserEntity) authentication.getPrincipal()).getId();
    }

    public static UserEntity getPrincipal(Authentication authentication) {
        return (UserEntity) authentication.getPrincipal();
    }


    public static String generateVerificationCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // Generate a random digit and append it to the StringBuilder
            sb.append(new Random().nextInt(10)); // Generates a random number between 0 (inclusive) and 10 (exclusive)
        }
        return sb.toString();
    }
}
