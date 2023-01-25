package dev.router.sisggarapi.adapter.mapper;


import dev.router.sisggarapi.adapter.request.supplierMovement.SupplierMovementRequest;
import dev.router.sisggarapi.adapter.response.productMovement.SupplierMovementResponse;
import dev.router.sisggarapi.core.domain.SupplierMovement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierMovementMapper {

    private final ModelMapper mapper;

    public SupplierMovement toSupplierMovement(SupplierMovementRequest request){
        return mapper.map(request, SupplierMovement.class);
    }

    public SupplierMovementResponse toSupplierMovementResponse(SupplierMovement supplierMovement) {
        return mapper.map(supplierMovement, SupplierMovementResponse.class);
    }

    public List<SupplierMovementResponse> toSupplierMovementResponseList(List<SupplierMovement> supplierMovementList) {
        return supplierMovementList.stream()
                .map(this::toSupplierMovementResponse)
                .collect(Collectors.toList());
    }
}
