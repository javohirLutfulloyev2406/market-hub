package uz.com.markethub.security.execption;

import org.springframework.security.core.AuthenticationException;

public class UserNotAuthorizedException extends AuthenticationException {
    public UserNotAuthorizedException(String msg) {
        super(msg);
    }
}
