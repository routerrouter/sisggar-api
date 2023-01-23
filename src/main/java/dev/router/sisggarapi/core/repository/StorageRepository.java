package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StorageRepository extends JpaRepository<Storage, UUID>, JpaSpecificationExecutor<Storage> {
    @Modifying
    @Query(value = "UPDATE tb_storage set storage_status='INACTIVE' WHERE storage_id= :storageId", nativeQuery = true)
    void delete(@Param("storageId") UUID storageId);

    Optional<Storage> findByDescription(@Param("description") String description);

    @Query(value = "Select * from tb_storage " +
            "where (LOWER(description) like LOWER(CONCAT('%',:description,'%'))) " +
            "order BY description asc",nativeQuery = true)
    List<Storage> findAll(@Param("description") String description);

}
