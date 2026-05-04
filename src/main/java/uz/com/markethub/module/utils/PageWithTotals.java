package uz.com.markethub.module.utils;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class PageWithTotals<T, U> {

    private List<T> data;
    private U totals;

    // Pagination fields
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public PageWithTotals() {}

    public PageWithTotals(List<T> data, U totals, long totalElements, int totalPages, int pageNumber, int pageSize) {
        this.data = data;
        this.totals = totals;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static <T, U> PageWithTotals<T, U> of(Page<T> page, U totals) {
        return new PageWithTotals<>(
                page.getContent(),
                totals,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    // 🔹 Eski PaginationUtil bilan ishlashi uchun Page qaytaruvchi metod
    public Page<T> toPage(Pageable pageable) {
        return new PageImpl<>(data, pageable, totalElements);

    }


}
