package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.Provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderService {
    Provider createProvider(Provider provider);
    Provider updateProvider(Provider provider, UUID providerId);
    void updateStatus(Provider provider,UUID providerId);
    Optional<Provider> findById(UUID providerId);
    List<Provider> findAll(String name, String nif, String telephone);
}
