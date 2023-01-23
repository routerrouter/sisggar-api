package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Role;
import dev.router.sisggarapi.core.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleType name);
}
