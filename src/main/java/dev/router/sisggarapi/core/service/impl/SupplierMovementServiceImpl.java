package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.adapter.request.supplierMovement.MovementFindRequest;
import dev.router.sisggarapi.core.domain.SupplierMovement;
import dev.router.sisggarapi.core.domain.enums.MovementType;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.ProductMovementRepository;
import dev.router.sisggarapi.core.repository.StorageRepository;
import dev.router.sisggarapi.core.repository.UserRepository;
import dev.router.sisggarapi.core.service.ExistenceService;
import dev.router.sisggarapi.core.service.SupplierMovementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class SupplierMovementServiceImpl implements SupplierMovementService {

    @Autowired
    ProductMovementRepository productMovementRepository;

    @Autowired
    ExistenceService existenceService;

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public SupplierMovement createSupplierMovement(UUID storageId, SupplierMovement supplierMovement, UUID userId) {
        log.debug("POST saveMoviment productMovementModelDto received {} ", supplierMovement.toString());

        var storage = storageRepository.findById(storageId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Armazem não encontrado"));
        supplierMovement.setStorage(storage);

        supplierMovement.setStatus(Status.ACTIVE);
        supplierMovement.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        supplierMovement.setUser(userRepository.findById(userId).get());
        supplierMovement.setMovementMonth(supplierMovement.getMovementDate().getMonth());
        supplierMovement = productMovementRepository.save(supplierMovement);
        log.info("Movement save successful movementId {} ", supplierMovement.getSupplierMovementId());
        saveExistence(storageId,supplierMovement);

        return supplierMovement;
    }

    @Override
    public List<SupplierMovement> findAll(UUID storageId, MovementFindRequest findRequest) {


        List<SupplierMovement> supplierMovementRequestList = productMovementRepository.findAll(storageId,
                findRequest.getSupplierId(),
                findRequest.getInitialDate(),
                findRequest.getFinalDate(),
                findRequest.getMovementType(),
                findRequest.getProductType());

        return supplierMovementRequestList;
    }

    @Transactional
    @Override
    public void deleteSupplierMovement(UUID movementId) {
        var movement = productMovementRepository.findById(movementId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Movimento informado não existe!"));
        movement.setStatus(Status.DELETED);
        productMovementRepository.save(movement);
        movement.setMovementType(movement.getMovementType().equals(MovementType.INPUT)? MovementType.OUTPUT: MovementType.INPUT);
        saveExistence(movement.getStorage().getStorageId(),movement);
    }

    public void saveExistence(UUID storageId,SupplierMovement movement) {
        log.info("POST, saveExistence existenceDto {} ", movement.toString());
        var existence = new ExistenceRequest();
        existence.setStorageId(storageId);
        existence.setUserId(movement.getUser().getUserId());
        existence.setQuantity(movement.getQuantity());
        existence.setProductType(movement.getProductType());
        existence.setMovementType(movement.getMovementType());
        existenceService.updateStock(existence);
        log.info("POST, existence saved {} ", existence.toString());
    }
}
