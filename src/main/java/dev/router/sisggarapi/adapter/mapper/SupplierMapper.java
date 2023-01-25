package dev.router.sisggarapi.adapter.mapper;


import dev.router.sisggarapi.adapter.request.supplier.SupplierRequest;
import dev.router.sisggarapi.adapter.response.supplier.SupplierResponse;
import dev.router.sisggarapi.core.domain.Supplier;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierMapper {

    private final ModelMapper mapper;

    public Supplier toSupplier(SupplierRequest request){
        return mapper.map(request, Supplier.class);
    }

    public SupplierResponse toSupplierResponse(Supplier supplier) {
        return mapper.map(supplier, SupplierResponse.class);
    }

    public List<SupplierResponse> toSupplierResponseList(List<Supplier> Suppliers) {
        return Suppliers.stream()
                .map(this::toSupplierResponse)
                .collect(Collectors.toList());
    }
}
