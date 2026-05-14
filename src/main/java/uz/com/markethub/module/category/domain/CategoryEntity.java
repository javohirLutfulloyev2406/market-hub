package uz.com.markethub.module.category.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.core.config.listener.HistoryListener;
import uz.com.markethub.core.domain.AbstractAuditEntity;
import uz.com.markethub.core.enums.AcceptLanguage;
import uz.com.markethub.core.util.DateUtils;
import uz.com.markethub.module.User.util.HelperRequestUtil;
import uz.com.markethub.module.category.dto.CategoryDTO;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "categories", schema = "product_sch")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HistoryListener.class)
public class CategoryEntity extends AbstractAuditEntity<Long> implements Serializable {

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parentCategory;

    public static CategoryEntity mapFromId(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return CategoryEntity.builder()
                .id(id)
                .build();
    }

    public CategoryDTO map2DTO() {
        return CategoryDTO.builder()
                .id(super.id)
                .name(getNameByLanguage())
                .description(this.description)
                .parentId(Objects.nonNull(this.parentCategory) ? this.parentCategory.getId() : null)
                .build();
    }

    public CategoryDTO.Full map2FullDTO() {
        return CategoryDTO.Full.builder()
                .id(super.id)
                .nameUz(this.nameUz)
                .nameEn(this.nameEn)
                .nameRu(this.nameRu)
                .description(this.description)
                .parentId(Objects.nonNull(this.parentCategory) ? this.parentCategory.getId() : null)
                .parentName(Objects.nonNull(this.parentCategory) ? this.parentCategory.getNameByLanguage() : null)
                .createdAt(Objects.nonNull(super.createdAt) ? DateUtils.convertToMillis(this.createdAt) : null)
                .createdBy(super.createdBy)
                .updatedAt(Objects.nonNull(super.updatedAt) ? DateUtils.convertToMillis(this.updatedAt) : null)
                .updatedBy(super.updatedBy)
                .build();
    }

    public String getNameByLanguage() {
        AcceptLanguage acceptLanguage = HelperRequestUtil.getAcceptLanguage();
        if (acceptLanguage == null) return this.nameUz;
        return switch (acceptLanguage) {
            case RU -> this.nameRu;
            case EN -> this.nameEn;
            default -> this.nameUz;
        };
    }
}