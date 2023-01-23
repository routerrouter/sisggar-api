package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Role;
import dev.router.sisggarapi.core.domain.enums.RoleType;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(RoleType roleType);
}
