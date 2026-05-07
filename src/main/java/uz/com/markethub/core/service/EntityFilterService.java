package uz.com.markethub.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;


public interface EntityFilterService {

    Page<?> filter(String entity, MultiValueMap<String, String> filters, Pageable pageable, Long logId);

}