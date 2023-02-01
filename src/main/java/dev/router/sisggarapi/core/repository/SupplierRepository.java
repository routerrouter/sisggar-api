package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID>, JpaSpecificationExecutor<Supplier> {
    Optional<Supplier> findByName(@Param("name") String name);
    Optional<Supplier> findByNif(@Param("nif") String nif);

    @Query(value = "select * from tb_supplier p where  LOWER(p.name) like LOWER(CONCAT('%',:name,'%')) " +
            "AND p.telephone like CONCAT('%',:telephone,'%') " +
            "AND p.nif like CONCAT('%',:nif,'%')",nativeQuery = true)
    List<Supplier> findAll(@Param("name") String name,
                           @Param("nif") String nif,
                           @Param("telephone") String telephone);
}
