package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.DeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>, JpaSpecificationExecutor<DeliveryManager> {
    @Modifying
    @Query(value = "UPDATE tb_deliverymanager set status='INACTIVE' WHERE delivery_manager_Id = :deliveryManagerId", nativeQuery = true)
    void deleteManager(@Param("deliveryManagerId") UUID deliveryManagerId);

    @Query(value = "SELECT  * from tb_deliverymanager WHERE storage_id = :storageId", nativeQuery = true)
    List<DeliveryManager> findAllByStorage(@Param("storageId") UUID storageId);

    Optional<DeliveryManager> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query(value = "Select * from tb_deliverymanager  " +
            "where storage_id=:storageId and LOWER(full_name)  LIKE CONCAT('%',:fullName,'%')", nativeQuery = true)
    List<DeliveryManager> findAll(@Param("storageId") UUID storageId, @Param("fullName") String fullName);
}
