package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Role;
import dev.router.sisggarapi.core.domain.enums.RoleType;
import dev.router.sisggarapi.core.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Optional<Role> findByRoleName(RoleType roleType) {
        return Optional.empty();
    }
}
