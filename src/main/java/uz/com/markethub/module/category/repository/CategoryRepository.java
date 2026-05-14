package uz.com.markethub.module.category.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.category.domain.CategoryEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends GenericRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c WHERE c.parentCategory IS NULL AND c.deleted = false")
    List<CategoryEntity> findAllRootCategories();

    boolean existsByNameUzAndDeletedFalse(String nameUz);

    boolean existsByNameUzAndIdNotAndDeletedFalse(String nameUz, Long id);
}