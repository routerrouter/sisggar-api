package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.adapter.mapper.ExistenceMapper;
import dev.router.sisggarapi.core.domain.Existence;
import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.repository.ExistenceRepository;
import dev.router.sisggarapi.core.repository.StorageRepository;
import dev.router.sisggarapi.core.service.ExistenceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class ExistenceServiceImpl implements ExistenceService {


    private final ExistenceRepository existenceRepository;
    private final StorageRepository storageRepository;
    private final ExistenceMapper mapper;

    public ExistenceServiceImpl(ExistenceRepository existenceRepository, StorageRepository storageRepository, ExistenceMapper mapper) {
        this.existenceRepository = existenceRepository;
        this.storageRepository = storageRepository;
        this.mapper = mapper;
    }

    @Override
    public Existence saveExistence(ExistenceRequest request) {
        log.debug("POST saveExistence existenceRequest received {} ", request.toString());
        var existence = new Existence(request);
        Long newQuantity = 0L;
        var otpStorage = storageRepository.findById(request.getStorageId())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Armazem não encontrado!");});
        var optExistence = existenceRepository.findExistenceModelByStorageAndProductType(otpStorage, request.getProductType());
        if (optExistence.isPresent()) {
            newQuantity = optExistence.get().updateQuantity(request.getMovementType(), existence.getQuantity());
            existence.setQuantity(newQuantity);
            existence.setExistenceId(optExistence.get().getExistenceId());
            existenceRepository.save(existence);
        } else {
            existenceRepository.save(existence);
        }

        return existence;
    }

    @Override
    public List<Existence> findAll(UUID storageId) {
        return existenceRepository.findExistenceModelByStorageId(storageId)
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void transferBetweenStorage(UUID storageOrigin, ExistenceRequest existence) {
        // Tipo da garrafa
        // Quantidade a transferir
        // Armazem de origem --> OUTPUT
        // Armazem de destino --> INPUT

        //Salvar movimento de OUTPUT para o armazem de origem

        //Salvar o movimento de INPUT para o armazem de destino
        saveInputTransference(existence);
        saveOutputTransference(storageOrigin, existence);


    }


    public void saveOutputTransference(UUID storageOrigin, ExistenceRequest existence) {
        log.info("Saving output existence for productType {} ", existence.getProductType());
        existence.setStorageId(storageOrigin);
        existence.setMovementType(MovementType.OUTPUT);
        saveExistence(existence);
        /*var otpStorage = storageRepository.findById(storageOrigin)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Armazem não encontrado!");});
        var optExistence = existenceRepository.findExistenceModelByStorageAndProductType(otpStorage, existence.getProductType());
        if (optExistence.isPresent()) {
            Long newQuantity = optExistence.get().updateQuantity(MovementType.OUTPUT, existence.getQuantity());
            existenceRepository.existenceTransfer(storageOrigin, existence.getProductType().toString(), newQuantity);
        }*/

    }

    public void saveInputTransference(ExistenceRequest existence) {
        log.info("Saving input existence for productType {} ", existence.getProductType());
        existence.setMovementType(MovementType.INPUT);
        saveExistence(existence);
        /*var otpStorage = storageRepository.findById(existence.getStorageId())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Armazem não encontrado!");});
        var optExistence = existenceRepository.findExistenceModelByStorageAndProductType(otpStorage, existence.getProductType());
        if (optExistence.isPresent()) {
            Long newQuantity = optExistence.get().updateQuantity(MovementType.INPUT, existence.getQuantity());
            existenceRepository.existenceTransfer(existence.getStorageId(), existence.getProductType().toString(), newQuantity);
        } else {
            saveExistence(existence);
        }*/

    }

}
