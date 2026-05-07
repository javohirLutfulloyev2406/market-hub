package uz.com.markethub.core.util;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class NumberUtils {

    public static final String P2P_UID = "p2p_";
    public static final String PAY_UID = "pay_";

    public static Boolean allNotNullOrEmpty(Object... values) {
        if (values == null) {
            return false;
        }
        for (Object value : values) {
            if (value == null) {
                return false;
            }
            if (value instanceof String && ((String) value).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static Boolean isAnyNullOrEmpty(Object... values) {
        if (values == null) {
            return true;
        }
        for (Object value : values) {
            if (value == null) {
                return true;
            }
            if (value instanceof String && ((String) value).trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static Boolean anyNotNullOrEmpty(Object... values) {

        if (values == null) {
            return false;
        }
        for (Object value : values) {
            if (value != null) {
                if (value instanceof String && ((String) value).trim().isEmpty()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public static Boolean allFieldsNotNullOrEmpty(Object... values) {
        if (values == null) {
            return false;
        }
        for (Object value : values) {
            for (Field field : value.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(value);
                    if (fieldValue == null) {
                        return false;
                    }
                    if (fieldValue instanceof String && ((String) fieldValue).trim().isEmpty()) {
                        return false;
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return true;
    }

    public static Boolean existParam(String param) {
        if (param == null)
            return Boolean.FALSE;
        if (param.isEmpty())
            return Boolean.FALSE;
        if (param.equalsIgnoreCase("null"))
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public static String getUID() {
        String uid = UUID.randomUUID().toString();
        return uid.replaceAll("-", "");
    }

    public static String getP2PUID() {
        return P2P_UID + getUID();
    }

    public static String getPayUID() {
        return PAY_UID + getUID();
    }

    public static BigDecimal getAmount(Long amountInTiyin) {
        if (amountInTiyin == null)
            return BigDecimal.ZERO;
        return BigDecimal.valueOf(amountInTiyin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
