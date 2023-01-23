package dev.router.sisggarapi.adapter.mapper;


import dev.router.sisggarapi.adapter.request.provider.ProviderRequest;
import dev.router.sisggarapi.adapter.response.provider.ProviderResponse;
import dev.router.sisggarapi.core.domain.Provider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProviderMapper {

    private final ModelMapper mapper;

    public Provider toProvider(ProviderRequest request){
        return mapper.map(request, Provider.class);
    }

    public ProviderResponse toProviderResponse(Provider provider) {
        return mapper.map(provider, ProviderResponse.class);
    }

    public List<ProviderResponse> toProviderResponseList(List<Provider> providers) {
        return providers.stream()
                .map(this::toProviderResponse)
                .collect(Collectors.toList());
    }
}
