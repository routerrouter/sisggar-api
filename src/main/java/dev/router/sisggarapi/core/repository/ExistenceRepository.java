package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Existence;
import dev.router.sisggarapi.core.domain.Storage;
import dev.router.sisggarapi.core.domain.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExistenceRepository extends JpaRepository<Existence, UUID> {

    Optional<Existence> findExistenceModelByStorageAndProductType(Storage storageId, ProductType productType);

    @Query("select e from Existence e where e.storage.storageId=:storageId")
    List<Existence> findExistenceModelByStorageId(UUID storageId);

    @Modifying
    @Query(value = "Update tb_existence set quantity=:quantity WHERE storage_id=:storageId AND product_type like :productType", nativeQuery = true)
     void  existenceTransfer(@Param("storageId") UUID storageId,
                                @Param("productType") String productType,
                                @Param("quantity") Long quantity);

}
