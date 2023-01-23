package dev.router.sisggarapi.adapter.response.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class StorageResponse {

    private UUID storageId;
    private String description;
    private Integer storageLimit;
    private Status status;

}
