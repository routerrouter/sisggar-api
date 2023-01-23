package dev.router.sisggarapi.adapter.response.company;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CompanyResponse {

    private UUID companyId;
    private String name;
    private String nif;
    private String address;
    private String telephone;
    private String email;
    private String imageUrl;
}
