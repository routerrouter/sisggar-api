package dev.router.sisggarapi.adapter.mapper;

import dev.router.sisggarapi.adapter.request.storage.StorageRequest;
import dev.router.sisggarapi.adapter.response.storage.StorageResponse;
import dev.router.sisggarapi.core.domain.Storage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StorageMapper {

    private final ModelMapper mapper;

    public Storage toStorage(StorageRequest request){
        return mapper.map(request, Storage.class);
    }

    public StorageResponse toStorageResponse(Storage storage) {
        return mapper.map(storage, StorageResponse.class);
    }

    public List<StorageResponse> toStorageResponseList(List<Storage> storages) {
        return storages.stream()
                .map(this::toStorageResponse)
                .collect(Collectors.toList());
    }
}
