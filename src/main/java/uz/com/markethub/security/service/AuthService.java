package uz.com.markethub.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.com.markethub.security.dto.AccessTokenDTO;
import uz.com.markethub.security.vm.LoginVM;


public interface AuthService {
    AccessTokenDTO authenticate(Long logId, LoginVM vm, HttpServletRequest request, HttpServletResponse response);
}
