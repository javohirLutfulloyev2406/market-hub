package uz.com.markethub.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.markethub.core.domain.HistoryEntity;
import uz.com.markethub.core.repository.HistoryRepository;
import uz.com.markethub.core.service.HistoryService;

@Log4j2
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository repository;

    @Override
    @Transactional
    public HistoryEntity create(HistoryEntity entity) {
        log.debug("Request -- create history data: {}", entity);

        repository.save(entity);

        log.debug("Response -- history data: {}", entity);
        return entity;
    }
}
