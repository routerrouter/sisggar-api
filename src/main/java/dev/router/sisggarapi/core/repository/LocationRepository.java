package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository  extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {
    @Modifying
    @Query(value = "UPDATE tb_location set location_status='INACTIVE' WHERE location_id = :locationId", nativeQuery = true)
    void delete(@Param("locationId") UUID locationId);

    Optional<Location> findByDescription(@Param("description") String description);

    @Query(value = "Select * from tb_location l where storage_id=:storageId and LOWER(description)  LIKE LOWER(CONCAT('%',:description,'%'))", nativeQuery = true)
    Page<Location> findAll(@Param("storageId") UUID storageId, @Param("description") String description, Pageable pageable);
}
