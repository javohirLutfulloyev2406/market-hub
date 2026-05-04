package uz.com.markethub.module.User.util;

import java.util.UUID;

public class ApiPartnerUtil {

    public static String generateFormattedKey() {
        return "APIK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}
