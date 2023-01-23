package dev.router.sisggarapi.adapter.mapper;


import dev.router.sisggarapi.adapter.request.deliverManager.DeliveryManagerRequest;
import dev.router.sisggarapi.adapter.response.deliverManager.DeliveryManagerResponse;
import dev.router.sisggarapi.core.domain.DeliveryManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeliveryManagerMapper {

    private final ModelMapper mapper;

    public DeliveryManager toDeliveryManager(DeliveryManagerRequest request){
        return mapper.map(request, DeliveryManager.class);
    }

    public DeliveryManagerResponse toDeliveryManagerResponse(DeliveryManager DeliveryManager) {
        return mapper.map(DeliveryManager, DeliveryManagerResponse.class);
    }

    public List<DeliveryManagerResponse> toDeliveryManagerResponseList(List<DeliveryManager> deliveryManagers) {
        return deliveryManagers.stream()
                .map(this::toDeliveryManagerResponse)
                .collect(Collectors.toList());
    }
}
