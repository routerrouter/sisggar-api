package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Provider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID>, JpaSpecificationExecutor<Provider> {
    Optional<Provider> findByName(@Param("name") String name);
    Optional<Provider> findByNif(@Param("nif") String nif);

    @Query(value = "select * from tb_provider p where  LOWER(p.name) like LOWER(CONCAT('%',:name,'%')) " +
            "AND p.telephone like CONCAT('%',:telephone,'%') " +
            "AND p.nif like CONCAT('%',:nif,'%')",nativeQuery = true)
    List<Provider> findAll(@Param("name") String name,
                           @Param("nif") String nif,
                           @Param("telephone") String telephone);
}
