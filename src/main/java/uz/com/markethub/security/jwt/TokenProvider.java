package uz.com.markethub.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.module.User.domain.UserEntity;
import uz.com.markethub.module.User.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Transactional(readOnly = true)
public class TokenProvider implements InitializingBean {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("#{new Long('${app.jwt.expire}')}")
    private Long jwtExpire;
    private Key key;

    @Autowired
    @Lazy
    UserRepository userRepository;
    private static final String LOG_ID = "logId";

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication authentication) {
        Date validity = new Date(System.currentTimeMillis() + jwtExpire);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String generateSystemAccessToken(String username) {
        Date validity = new Date(System.currentTimeMillis() + jwtExpire);

        return Jwts.builder()
                .setSubject(username)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromAccessToken(token);

        UserEntity user = userRepository.findOneByUsername(claims.getSubject())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found by username: " + claims.getSubject()));

        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    public Claims getClaimsFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT token");
            return false;
        }
    }
}
