package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query(value="select case when count(c) > 0 THEN true ELSE false END FROM tb_company c ",nativeQuery = true)
    boolean existCompany();

    @Query(value ="select * from tb_company c limit 1",nativeQuery = true)
    Company findCompany();

}
