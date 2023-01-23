package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Costumer;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.CostumerRepository;
import dev.router.sisggarapi.core.repository.DeliveryManagerRepository;
import dev.router.sisggarapi.core.repository.LocationRepository;
import dev.router.sisggarapi.core.service.CostumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CostumerServiceImpl implements CostumerService {

    @Autowired
    CostumerRepository costumerRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    DeliveryManagerRepository deliveryManagerRepository;

    @Override
    public Costumer createCostumer(Costumer costumer) {

        var costumerOptional = costumerRepository.findByName(costumer.getName());
        if (costumerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um Cliente com este nome. Por favor verificar e tentar novamente.");
        }
        costumer.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        costumer.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        costumer.setPossession(0);
        costumer.setCostumerStatus(Status.ACTIVE);
        costumer = costumerRepository.save(costumer);

        return costumer;

    }


    @Override
    public Costumer updateCostumer(Costumer costumer, UUID costumerId) {

        var costumerModel = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado!"));

        costumerModel.setAddress(costumer.getAddress());
        costumerModel.setName(costumer.getName());
        costumerModel.setNif(costumer.getNif());
        costumerModel.setEmail(costumer.getEmail());
        costumerModel.setTelephone(costumer.getTelephone());
        costumerModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return costumerRepository.save(costumerModel);

    }

    @Override
    public void updatePossession(Costumer costumer, UUID costumerId) {
        log.info("Actualizando Posse do cliente {}", costumerId);
        var costumerModel = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado!"));
        var oldPossession = costumerModel.getPossession();
        costumerModel.setPossession(costumer.getPossession());
        costumerModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        costumerRepository.save(costumerModel);
        log.info("Posse do cliente {} actualizado de {} para {} com sucesso!", costumerId, oldPossession, costumer.getPossession());

    }

    @Override
    public void modifyStatus(Costumer costumer, UUID costumerId) {
        var costumerModel = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado!"));

        costumerModel.setCostumerStatus(costumer.getCostumerStatus());
        costumerModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        costumerRepository.save(costumerModel);
    }

    @Override
    public void updateLimit(Costumer costumer, UUID costumerId) {
        log.info("Actualizando Limite de entrega do cliente {}", costumerId);
        var costumerModel = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado!"));
        var oldLimit = costumerModel.getDeliveryLimit();
        costumerModel.setDeliveryLimit(costumer.getDeliveryLimit());
        costumerModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        costumerRepository.save(costumerModel);
        log.info("Posse do cliente {} actualizado de {} para {} com sucesso!", costumerId, oldLimit, costumer.getDeliveryLimit());
    }

    @Transactional
    @Override
    public void removeCostumerIntoDeliveryManager(UUID costumerId, UUID deliveryManagerId) {
        if (costumerRepository.isRelatedCostumerWithDeliveryManager(costumerId, deliveryManagerId) == 0) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não está associado ao entregador!");
        } else {
            costumerRepository.removeIntoDeliveryManager(costumerId, deliveryManagerId);
        }

    }

    @Transactional
    @Override
    public void removeCostumerIntoLocation(UUID costumerId, UUID locationId) {
        if (costumerRepository.isRelatedCostumerWithLocation(costumerId, locationId) == 0) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não está associado a esta localidade");
        } else {
            costumerRepository.removeLocationIntoCostumer(costumerId, locationId);
        }
    }

    @Override
    public void addIntoLocation(UUID costumerId, ArrayList<UUID> locations) {
        locations.forEach(location -> {
            addLocation(costumerId, location);
        });
    }

    @Override
    public void addIntoDeliveryManager(UUID costumerId, ArrayList<UUID> deliveryManagers) {
        deliveryManagers.forEach(deliveryManager -> {
            addDeliveryManager(costumerId, deliveryManager);
        });
    }


    @Override
    public Optional<Costumer> findById(UUID costumerId) {
        var costumer = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado!"));

        return Optional.of(costumer);
    }


    @Override
    public List<Costumer> findAll(String name, String nif, String telephone, UUID locationId, UUID storageId) {
        List<Costumer> costumerList = new ArrayList<>();
        if (locationId != null) {
            costumerList = costumerRepository.findAllByLocation(name, nif, telephone, storageId, locationId);
        } else {
            costumerList = costumerRepository.findAll(name, nif, telephone, storageId);
        }

        return costumerList;

    }


    @Override
    public boolean existsByCostumerAndLocation(UUID costumerId, UUID locationId) {
        return costumerRepository.existCostumerAndLocation(costumerId, locationId);
    }

    @Override
    public boolean existsByCostumerAndDeliveryManager(UUID costumerId, UUID deliveryManagerId) {
        return costumerRepository.existCostumerAndDeliveryManagers(costumerId, deliveryManagerId);
    }


    public void addLocation(UUID costumerId, UUID locationId) {
        if (existsByCostumerAndLocation(costumerId, locationId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já associado a esta localidade");
        } else {
            var locationOptional = locationRepository.findById(locationId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidade não existe!"));

            costumerRepository.addLocation(costumerId, locationOptional.getLocationId());
        }
    }

    public void addDeliveryManager(UUID costumerId, UUID deliveryManagerId) {
        if (existsByCostumerAndDeliveryManager(costumerId, deliveryManagerId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já está associado ao gerente selecionado.");
        } else {
            costumerRepository.addDeliveries(costumerId, deliveryManagerId);
        }
    }



}
