package uz.com.markethub.security.vm;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerificationVM {
    @NotNull
    private Long id;

    @NotNull
    private String verificationCode;
}
