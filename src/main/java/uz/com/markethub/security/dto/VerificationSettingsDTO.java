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
public class VerificationSettingsDTO {

    private int codeLength;
    private int attemptTimeInSeconds;
    private Integer enterWrongPasswordCount;
    private Integer verificationResendCount;


    public static VerificationSettingsDTO mapFrom(int length, int timeDuration, int enterWrongPasswordCount,
                                                  int verificationResendCount) {
        return VerificationSettingsDTO
                .builder()
                .codeLength(length)
                .attemptTimeInSeconds(timeDuration)
                .enterWrongPasswordCount(enterWrongPasswordCount)
                .verificationResendCount(verificationResendCount)
                .build();
    }
}
