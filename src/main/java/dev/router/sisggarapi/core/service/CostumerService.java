package dev.router.sisggarapi.core.service;


import dev.router.sisggarapi.core.domain.Costumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CostumerService {
    Costumer createCostumer(Costumer costumer);
    Costumer updateCostumer(Costumer costumer, UUID costumerId);
    void updatePossession(Costumer costumer, UUID costumerId);
    void modifyStatus(Costumer costumer, UUID costumerId);
    void updateLimit(Costumer costumer, UUID costumerId);
    void removeCostumerIntoDeliveryManager(UUID costumerId, UUID deliveryManagerId);
    void removeCostumerIntoLocation(UUID costumerId, UUID locationId);
    void addIntoLocation(UUID costumerId, ArrayList<UUID> locations);
    void addIntoDeliveryManager(UUID costumerId, ArrayList<UUID> deliveryManagers);
    boolean existsByCostumerAndLocation(UUID costumerId, UUID locationId);
    boolean existsByCostumerAndDeliveryManager(UUID costumerId, UUID deliveryManagerId);
    Optional<Costumer> findById(UUID costumerId);
    List<Costumer> findAll(String name, String nif, String telephone, UUID locationId, UUID storageId);
}
