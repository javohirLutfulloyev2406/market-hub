package uz.com.markethub.security.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class LoginVM {
    @NotBlank
    @Size(min = 5, max = 180)
    private String username;

    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    private String deviceName;

    private String privateIpAddress;

    private String macAddress;

    private String osVersion;

    private String imei;

}
