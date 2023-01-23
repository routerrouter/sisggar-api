package dev.router.sisggarapi.adapter.response.costumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.router.sisggarapi.adapter.response.deliverManager.DeliveryManagerResponse;
import dev.router.sisggarapi.adapter.response.location.LocationResponse;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostumerResponse {

    private UUID  costumerId;
    private String name;
    private String nif;
    private String telephone;
    private String address;
    private String email;
    private Integer deliveryLimit;
    private Integer possession;
    private Status costumerStatus;
    private ArrayList<LocationResponse> locationResponses;
    private ArrayList<DeliveryManagerResponse> deliveryManagerRequests;
    //Lista das movimentações ....

}
