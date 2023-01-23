package dev.router.sisggarapi.adapter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.dto.ProductMovementModelDto;
import dev.router.sisggarapi.core.service.ProductMovementService;
import dev.router.sisggarapi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/product-movement")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductMovementController {

    @Autowired
    ProductMovementService productMovementService;

    @PostMapping()
    public ResponseEntity<Object> createProvider(@RequestBody
                                                 @Valid @JsonView(ProductMovementModelDto.ProductMovementView.RegistrationPost.class)
                                                             ProductMovementModelDto productMovementModelDto, Errors errors) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productMovementService.createProductMovement(productMovementModelDto));
    }

    @GetMapping
    public ResponseEntity<Page<ProductMovementModelDto>> findAll(@RequestParam() UUID storageId,
                                                                 @RequestParam(required = false) UUID providerId,
                                                                 @RequestParam(required = false) String initialDate,
                                                                 @RequestParam(required = false)  String  finalDate,
                                                                 @RequestParam(required = false) String movementType,
                                                                 @RequestParam(required = false) String productType,
                                                                 @PageableDefault(page = 0, size = 10, sort = "product_movement_id", direction = Sort.Direction.ASC) Pageable pageable){
        var movementsList = productMovementService.findAll(storageId,providerId, DateUtils.stringToDate(initialDate), DateUtils.stringToDate(finalDate),movementType,productType);

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > movementsList.size() ? movementsList.size()
                : (start + pageable.getPageSize()));
        Page<ProductMovementModelDto> movementModelDtoPage = new PageImpl<ProductMovementModelDto>(movementsList.subList(start,end), pageable, movementsList.size());
        return  ResponseEntity.status(HttpStatus.OK).body(movementModelDtoPage);
    }
}
