package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.ProductMovement;
import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.adapter.dto.ProductMovementModelDto;
import dev.router.sisggarapi.core.repository.ProductMovementRepository;
import dev.router.sisggarapi.core.service.ExistenceService;
import dev.router.sisggarapi.core.service.ProductMovementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Service
public class ProductMovementServiceImpl implements ProductMovementService {

    @Autowired
    ProductMovementRepository productMovementRepository;

    @Autowired
    ExistenceService existenceService;

    @Transactional
    @Override
    public ProductMovementModelDto createProductMovement(ProductMovementModelDto modelDto) {
        log.debug("POST saveMoviment productMovementModelDto received {} ", modelDto.toString());
        var productMovement = new ProductMovement(modelDto);
        BeanUtils.copyProperties(modelDto,productMovement);
        productMovement.setMovementMonth(modelDto.getMovementDate().getMonth());
        productMovement = productMovementRepository.save(productMovement);
        log.info("Movement save successful movementId {} ", productMovement.getProductMovementId());
        BeanUtils.copyProperties(productMovement,modelDto);

        var existenceDto = new ExistenceRequest();
        existenceDto.setStorageId(modelDto.getStorageId());
        existenceDto.setUserId(modelDto.getUserModelId());
        existenceDto.setQuantity(modelDto.getQuantity());
        existenceDto.setProductType(modelDto.getProductType());
        existenceDto.setMovementType(modelDto.getMovementType());
        saveExistence(existenceDto);
        return modelDto;
    }

    @Override
    public List<ProductMovementModelDto> findAll(UUID storageId,
                                                 UUID providerId,
                                                 LocalDate initialDate,
                                                 LocalDate finalDate,
                                                 String movementType,
                                                 String productType) {


        Iterable<ProductMovement> itreable = productMovementRepository.findAll(storageId,providerId,
                    initialDate,finalDate,movementType,productType);


        List<ProductMovementModelDto> productMovementModelDtoList = StreamSupport.stream(itreable.spliterator(), false).map(list -> {
            var dto = new ProductMovementModelDto();
            BeanUtils.copyProperties(list, dto);
            return dto;
        }).collect(Collectors.toList());
        return productMovementModelDtoList;
    }

    public void saveExistence(ExistenceRequest existenceRequest) {
        log.info("POST, saveExistence existenceDto {} ", existenceRequest.toString());
        existenceService.saveExistence(existenceRequest);
    }
}
