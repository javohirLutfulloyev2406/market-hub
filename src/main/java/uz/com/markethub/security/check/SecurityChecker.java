package uz.com.markethub.security.check;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import uz.com.markethub.module.User.service.ApiPartnerService;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.security.util.SecurityUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class SecurityChecker {
    private final ApiPartnerService apiPartnerService;

    public boolean hasAuthorityAndCheckDevice(String authority) {
        apiPartnerService
                .checkIsEnabledAndCorrectVersion(
                        HelperRequestUtil.getApiPartnerInfo()
                );

        if (SecurityUtils.hasAuthorityInRole(authority)) {
            return true;
        }

        return false;
    }

}
