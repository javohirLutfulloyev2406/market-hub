package uz.com.markethub.module.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import uz.com.markethub.module.category.domain.CategoryEntity;

import java.io.Serializable;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Long parentId;

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Full extends CategoryDTO {
        private String nameUz;
        private String nameEn;
        private String nameRu;
        private String parentName;
        private Long createdAt;
        private String createdBy;
        private Long updatedAt;
        private String updatedBy;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class CreateOrUpdate extends CategoryDTO {

        @NotBlank
        private String nameUz;

        @NotBlank
        private String nameEn;

        @NotBlank
        private String nameRu;

        public CategoryEntity map2Entity(CategoryEntity parentCategory) {
            return CategoryEntity.builder()
                    .nameUz(this.nameUz)
                    .nameEn(this.nameEn)
                    .nameRu(this.nameRu)
                    .description(this.getDescription())
                    .parentCategory(parentCategory)
                    .build();
        }

        public void set2Entity(CategoryEntity entity, CategoryEntity parentCategory) {
            entity.setNameUz(this.nameUz);
            entity.setNameEn(this.nameEn);
            entity.setNameRu(this.nameRu);
            entity.setDescription(this.getDescription());
            entity.setParentCategory(parentCategory);
        }
    }
}