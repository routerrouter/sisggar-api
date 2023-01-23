package dev.router.sisggarapi.core.service.impl;


import dev.router.sisggarapi.core.domain.Storage;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.StorageRepository;
import dev.router.sisggarapi.core.service.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class StorageServiceImpl implements StorageService {

    private StorageRepository storageRepository;

    StorageServiceImpl(StorageRepository storageRepository){
        this.storageRepository = storageRepository;
    }


    @Override
    public Storage createStorage(Storage storage) {
        log.debug("POST saveStorage storageDto received {} ", storage.toString());

        Optional<Storage> optStorage = storageRepository.findByDescription(storage.getDescription());
        if (optStorage.isPresent()){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um Armazem com esta descrição. Favor selecionar outra descrição.");
        }

        storage.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        storage.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        storage.setStorageStatus(Status.ACTIVE);
        storage = storageRepository.save(storage);
        log.debug("POST saveStorage storageId saved {} ", storage.getStorageId());
        log.info("Storage saved successfully storageId {} ", storage.getStorageId());
        return storage;
    }

    @Override
    public Storage updateStorage(Storage storage, UUID storageId) {
        log.debug("PUT updateStorage storageDto received {} ", storage.toString());
        Storage storageToEdit = storageRepository.findById(storageId)
                .orElseThrow(()-> new  ResponseStatusException(HttpStatus.NOT_FOUND,"Armazem não encontrado."));
        storageToEdit.setStorageId(storageId);
        storageToEdit.setDescription(storage.getDescription());
        storageToEdit.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        storageToEdit.setStorageLimit(storage.getStorageLimit());
        storageToEdit = storageRepository.save(storageToEdit);
        log.debug("PUT updateStorage storageId saved {} ", storageId);
        log.info("Storage updated successfully storageId {} ", storageId);
        return storageToEdit;
    }

    @Transactional
    @Override
    public void delete(UUID storageId) {
        log.debug("DELETE deleteStorage storageId received {} ", storageId);
        Optional<Storage> optStorage = storageRepository.findById(storageId);
        if(!optStorage.isPresent()) {
             new ResponseStatusException(HttpStatus.NOT_FOUND,"Armazem não encontrado.");
        }
        storageRepository.delete(storageId);
        log.debug("DELETE deleteStorage storageId deleted {} ", storageId);
        log.info("Storage deleted successfully storageId {} ", storageId);
        storageRepository.delete(storageId);
    }

    @Override
    public Optional<Storage> findById(UUID storageId) {
        var storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Armazem não encontrado!"));

        return Optional.of(storage);
    }

    @Override
    public Optional<Storage> findByDescription(String description) {
        return storageRepository.findByDescription(description);
    }

    @Override
    public List<Storage> findAll(String description) {
        return storageRepository.findAll(description);
    }
}
