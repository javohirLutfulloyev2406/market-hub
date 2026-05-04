package uz.com.markethub.module.User.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import uz.com.markethub.core.util.HelperUtil;
import uz.com.markethub.module.User.dto.UserDTO;
import uz.com.markethub.module.User.service.UserService;
import uz.com.markethub.module.User.service.facade.UserServiceCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceCacheImpl implements UserServiceCache {
    private final UserService service;
    private final Map<String, UserDTO.Full> userCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initData() {
        Long logId = HelperUtil.generateLogId();
        log.info("Request -- logId: {}, Initializing user cache...", logId);

        userCache.clear();
        List<UserDTO.Full> allUsers = service.findAll(logId);

        for (UserDTO.Full user : allUsers) {
            if (user.getName() != null) {
                userCache.put(user.getName().toLowerCase(), user);
            }
        }

        log.info("Response -- logId: {}, User cache initialized with {} users", logId, userCache.size());
    }

    @Override
    public List<UserDTO.Full> searchByName(String query, Long logId) {
        log.debug("Request -- logId: {}, search query: {}", logId, query);

        if (query == null || query.isBlank()) {
            log.debug("Response -- logId: {}, returning all {} users", logId, userCache.size());
            return new ArrayList<>(userCache.values());
        }

        String lower = query.toLowerCase();
        List<UserDTO.Full> result = userCache.entrySet().stream()
                .filter(entry -> entry.getKey().contains(lower))
                .map(Map.Entry::getValue)
                .toList();

        log.debug("Response -- logId: {}, found {} users for query '{}'", logId, result.size(), query);
        return result;
    }
}
