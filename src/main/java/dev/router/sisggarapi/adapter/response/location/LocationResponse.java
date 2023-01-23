package dev.router.sisggarapi.adapter.response.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.router.sisggarapi.adapter.response.storage.StorageResponse;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationResponse {
    private UUID locationId;
    private String description;
    private Status locationStatus;
    private StorageResponse response;
}
