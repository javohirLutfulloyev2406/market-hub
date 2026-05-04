package uz.com.markethub.module.fileSystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.com.markethub.core.repository.GenericRepository;
import uz.com.markethub.module.fileSystem.domain.FileEntity;


import java.util.List;

public interface FileRepository extends GenericRepository<FileEntity, Long> {

    FileEntity findByFileName(String fileName);

    @Query("SELECT r FROM FileEntity r WHERE r.id IN :ids AND r.deleted = false")
    List<FileEntity> findAllByIdIn(@Param("ids") List<Long> ids);


}
