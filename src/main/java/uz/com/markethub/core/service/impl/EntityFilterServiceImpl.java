package uz.com.markethub.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.core.service.EntityFilterService;
import uz.com.markethub.core.specification.GenericSpecifications;


import java.lang.reflect.Method;

@Log4j2
@Service
@RequiredArgsConstructor
public class EntityFilterServiceImpl implements EntityFilterService {

    private final ApplicationContext applicationContext;

    @Override
    public Page<?> filter(String entity, MultiValueMap<String, String> filters, Pageable pageable, Long logId) {

        filters.remove("entity");

        String simpleName = entity.replace("Entity", "");

        log.debug("Request -- logId: {}, simpleName id: {}", logId, simpleName);

        String repoBeanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1) + "Repository";

        log.debug("Request -- logId: {}, repoBeanName id: {}", logId, repoBeanName);

        GenericRepository<?, ?> repository = (GenericRepository<?, ?>) applicationContext.getBean(repoBeanName);

        Page<?> allEntity = repository.findAll(GenericSpecifications.byFilters(filters.toSingleValueMap()), pageable);

        return allEntity.map(e -> {
            try {
                Method m = e.getClass().getMethod("map2FullDTO");
                return m.invoke(e);
            } catch (Exception ex) {
                throw new RuntimeException("map2FullDTO method not found: " + e.getClass(), ex);
            }
        });
    }

}
