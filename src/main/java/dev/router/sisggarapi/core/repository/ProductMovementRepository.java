package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.SupplierMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductMovementRepository extends JpaRepository<SupplierMovement, UUID> {

    @Query(value="SELECT * from tb_suppliermovement sm\n" +
            "WHERE sm.movement_date BETWEEN :initialDate AND :finalDate \n" +
            "AND sm.storage_id=:storageId\n" +
            "AND (cast(:supplierId as UUID) IS NULL OR sm.supplier_id=:supplierId) \n" +
            "AND sm.movement_type LIKE CONCAT('%',:movementType,'%') \n" +
            "AND sm.product_type LIKE CONCAT('%',:productType,'%') ", nativeQuery = true)
    List<SupplierMovement> findAll(
            @Param("storageId") UUID storageId,
            @Param("supplierId") UUID supplierId,
            @Param("initialDate") LocalDate initialDate,
            @Param("finalDate") LocalDate finalDate,
            @Param("movementType") String movementType,
            @Param("productType") String productType);
}
