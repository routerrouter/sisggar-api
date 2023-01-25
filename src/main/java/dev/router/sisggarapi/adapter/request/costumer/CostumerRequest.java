package dev.router.sisggarapi.adapter.request.costumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.core.domain.DeliveryManager;
import dev.router.sisggarapi.core.domain.Location;
import dev.router.sisggarapi.core.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CostumerRequest {

    public interface CostumerView {
        public static interface RegistrationPost {}
        public static interface CostumerPut {}
        public static interface UpdateStatus {}
        public static interface UpdateLimit {}
        public static interface UpdatePossession {}
        public static interface LocationsAdd {}
        public static interface DeliveryManagersAdd {}
    }

    @NotBlank(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @JsonView({CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    private String name;

    @NotBlank(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @JsonView({CostumerView.RegistrationPost.class,  CostumerView.CostumerPut.class})
    @Size(min = 10, max = 16, groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    private String nif;

    @NotBlank(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @JsonView({CostumerView.RegistrationPost.class,  CostumerView.CostumerPut.class})
    private String telephone;

    @NotBlank(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @JsonView({CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    private String address;

    @NotBlank(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @JsonView({CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    @Email(groups = {CostumerView.RegistrationPost.class, CostumerView.CostumerPut.class})
    private String email;

    @JsonView({CostumerView.RegistrationPost.class, CostumerView.UpdateLimit.class})
    private Integer deliveryLimit;

    @NotNull(groups = {CostumerView.UpdatePossession.class})
    @JsonView({CostumerView.UpdatePossession.class})
    private Integer possession;

    @JsonView({CostumerView.UpdateStatus.class})
    private Status costumerStatus;

    @JsonView({CostumerView.LocationsAdd.class})
    private ArrayList<UUID> locations;

    @JsonView({CostumerView.DeliveryManagersAdd.class})
    private ArrayList<UUID> deliveryManagers;


}
