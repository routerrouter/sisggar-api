package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.ProductMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductMovementRepository extends JpaRepository<ProductMovement, UUID> {

    @Query(value="SELECT * from tb_productmovement pm\n" +
            "WHERE pm.movement_date BETWEEN :initialDate AND :finalDate \n" +
            "AND pm.storage_id=:storageId\n" +
            "AND (cast(:providerId as UUID) IS NULL OR pm.provider_id=:providerId) \n" +
            "AND pm.movement_type LIKE CONCAT('%',:movementType,'%') \n" +
            "AND pm.product_type LIKE CONCAT('%',:productType,'%') ", nativeQuery = true)
    List<ProductMovement> findAll(
            @Param("storageId") UUID storageId,
            @Param("providerId") UUID providerId,
            @Param("initialDate") LocalDate initialDate,
            @Param("finalDate") LocalDate finalDate,
            @Param("movementType") String movementType,
            @Param("productType") String productType);
}
