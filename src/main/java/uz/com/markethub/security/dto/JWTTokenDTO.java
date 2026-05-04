package uz.com.markethub.security.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JWTTokenDTO {

    private Long expiresIn;

    private String accessToken;
}
