package dev.router.sisggarapi.adapter.mapper;

import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.response.costumer.CostumerResponse;
import dev.router.sisggarapi.core.domain.Costumer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CostumerMapper {

    private final ModelMapper mapper;

    public Costumer toCostumer(CostumerRequest request){
        return mapper.map(request, Costumer.class);
    }

    public CostumerResponse toCostumerResponse(Costumer costumer) {
        return mapper.map(costumer, CostumerResponse.class);
    }

    public List<CostumerResponse> toCostumerResponseList(List<Costumer> costumers) {
        return costumers.stream()
                .map(this::toCostumerResponse)
                .collect(Collectors.toList());
    }
}
