package uz.com.markethub.module.User.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.com.markethub.core.enums.AcceptLanguage;
import uz.com.markethub.core.record.ApiStatus;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.core.util.NumberUtils;
import uz.com.markethub.module.User.record.PartnerInfoRecord;
import uz.com.markethub.security.execption.ApiPartnerException;

import java.util.Objects;

import static java.util.Objects.nonNull;

public class HelperRequestUtil {
    private HelperRequestUtil() {
    }

    public static final String API_PARTNER_CODE = "API-Partner-Code";
    public static final String API_PARTNER_VERSION = "API-Partner-Version";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    public static PartnerInfoRecord getApiPartnerInfo() {
        return extractPartnerInfo(getCurrentRequest());
    }

    public static AcceptLanguage getAcceptLanguage() {
        AcceptLanguage acceptLanguage;
        try {
            acceptLanguage = getAcceptLanguage(getCurrentRequest());
        } catch (Exception e) {
            acceptLanguage = AcceptLanguage.defaultLanguage();
        }
        return acceptLanguage;
    }

    public static AcceptLanguage getAcceptLanguage(HttpServletRequest request) {
        return extractAcceptLanguage(request);
    }

    public static PartnerInfoRecord getApiPartnerInfo(HttpServletRequest request) {
        return extractPartnerInfo(request);
    }

    public static String getApiPartnerVersion() {
        HttpServletRequest request = getCurrentRequest();

        String version = request.getHeader(API_PARTNER_VERSION);

        if (Objects.isNull(version)) {
            //todo exception message
            throw new RuntimeException("device ");
        }

        return version;
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
        return ((ServletRequestAttributes) attrs).getRequest();
    }

    public static String getIpaddress() {
        HttpServletRequest request = getCurrentRequest();
        return extractIp(request);
    }

    public static String getIpaddress(HttpServletRequest request) {
        return extractIp(request);
    }

    private static String extractIp(HttpServletRequest request) {
        String clientIp;
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private static PartnerInfoRecord extractPartnerInfo(HttpServletRequest request) {
        String code = request.getHeader(API_PARTNER_CODE);
        String version = request.getHeader(API_PARTNER_VERSION);

        if (!NumberUtils.existParam(code) || !NumberUtils.existParam(version)) {
            throw new ApiPartnerException(ApiStatus.ERR_API_PARTNER_VALIDATION);
        }
        return new PartnerInfoRecord(code, version);
    }

    private static AcceptLanguage extractAcceptLanguage(HttpServletRequest request) {
        String headerAcceptLanguage = request.getHeader(ACCEPT_LANGUAGE);

        return !NumberUtils.existParam(headerAcceptLanguage) ? AcceptLanguage.defaultLanguage() : HelperUtil.lookupEnumValue(AcceptLanguage.class, headerAcceptLanguage.toUpperCase());
    }

    private static String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }
}
