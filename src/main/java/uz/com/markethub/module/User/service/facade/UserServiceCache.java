package uz.com.markethub.module.User.service.facade;

import uz.com.markethub.module.User.dto.UserDTO;

import java.util.List;

public interface UserServiceCache {
    List<UserDTO.Full> searchByName(String query, Long logId);
}
