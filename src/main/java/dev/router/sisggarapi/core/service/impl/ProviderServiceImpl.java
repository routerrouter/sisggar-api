package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Provider;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.ProviderRepository;
import dev.router.sisggarapi.core.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    ProviderRepository providerRepository;

    @Override
    public Provider createProvider(Provider provider) {
        Optional<Provider> optProvider = providerRepository.findByName(provider.getName());
        if (optProvider.isPresent()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um fornecedor com este Nome!");
        }

        Optional<Provider> optProviderNif = providerRepository.findByNif(provider.getNif());
        if (optProviderNif.isPresent()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um fornecedor com este Nif!");
        }

        provider.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        provider.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        provider.setProviderStatus(Status.ACTIVE);
        provider = providerRepository.save(provider);

        return provider;
    }

    @Override
    public Provider updateProvider(Provider provider, UUID providerId) {

        var optProvider = providerRepository.findById(providerId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));

        optProvider.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        optProvider.setName(provider.getName());
        optProvider.setEmail(provider.getEmail());
        optProvider.setNif(provider.getNif());
        optProvider.setAddress(provider.getAddress());
        return providerRepository.save(optProvider);
    }

    @Override
    public void updateStatus(Provider provider, UUID providerId) {
        var optProvider = providerRepository.findById(providerId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));
        optProvider.setProviderStatus(provider.getProviderStatus());
        optProvider.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        providerRepository.save(optProvider);
    }

    @Override
    public Optional<Provider> findById(UUID providerId) {
        var optProvider = providerRepository.findById(providerId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));
        return Optional.of(optProvider);
    }

    @Override
    public List<Provider> findAll(String name, String nif, String telephone) {
        return providerRepository.findAll(name,nif,telephone);
    }
}
