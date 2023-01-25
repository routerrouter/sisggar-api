package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Supplier;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.repository.SupplierRepository;
import dev.router.sisggarapi.core.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        Optional<Supplier> optSupplier = supplierRepository.findByName(supplier.getName());
        if (optSupplier.isPresent()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um fornecedor com este Nome!");
        }

        Optional<Supplier> optSupplierNif = supplierRepository.findByNif(supplier.getNif());
        if (optSupplierNif.isPresent()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um fornecedor com este Nif!");
        }

        supplier.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        supplier.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        supplier.setSupplierStatus(Status.ACTIVE);
        supplier = supplierRepository.save(supplier);

        return supplier;
    }

    @Override
    public Supplier updateSupplier(Supplier supplier, UUID supplierId) {

        var optSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));

        optSupplier.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        optSupplier.setName(supplier.getName());
        optSupplier.setEmail(supplier.getEmail());
        optSupplier.setNif(supplier.getNif());
        optSupplier.setAddress(supplier.getAddress());
        return supplierRepository.save(optSupplier);
    }

    @Override
    public void updateStatus(Supplier supplier, UUID supplierId) {
        var optSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));
        optSupplier.setSupplierStatus(supplier.getSupplierStatus());
        optSupplier.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        supplierRepository.save(optSupplier);
    }

    @Override
    public Optional<Supplier> findById(UUID supplierId) {
        var optSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Fornecedor indicado não existe!"));
        return Optional.of(optSupplier);
    }

    @Override
    public List<Supplier> findAll(String name, String nif, String telephone) {
        return supplierRepository.findAll(name,nif,telephone);
    }
}
