package dev.router.sisggarapi.adapter.response.deliverManager;

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
public class DeliveryManagerResponse {

    private UUID deliveryManagerId;
    private String fullName;
    private String phoneNumber;
    private Status status;
    private StorageResponse storage;
}
