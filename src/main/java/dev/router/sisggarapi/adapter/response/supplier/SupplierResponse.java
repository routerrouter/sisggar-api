package dev.router.sisggarapi.adapter.response.supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierResponse {

    private UUID supplierId;
    private String name;
    private String nif;
    private String telephone;
    private String address;
    private String email;
    private Status supplierStatus;
}
