package uz.com.markethub.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class VerificationDTO {
    private Long id;
    private String phoneNumber;
    private String verificationCode;
    private VerificationSettingsDTO verificationSettings;
}
