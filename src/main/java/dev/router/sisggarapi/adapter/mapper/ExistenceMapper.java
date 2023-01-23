package dev.router.sisggarapi.adapter.mapper;

import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.adapter.response.existence.ExistenceResponse;
import dev.router.sisggarapi.core.domain.Existence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExistenceMapper {

    private final ModelMapper mapper;

    public Existence toExistence(ExistenceRequest request){
        return mapper.map(request, Existence.class);
    }


    public ExistenceResponse toExistenceResponse(Existence Existence) {
        return mapper.map(Existence, ExistenceResponse.class);
    }

    public List<ExistenceResponse> toExistenceResponseList(List<Existence> existences) {
        return existences.stream()
                .map(this::toExistenceResponse)
                .collect(Collectors.toList());
    }
}
