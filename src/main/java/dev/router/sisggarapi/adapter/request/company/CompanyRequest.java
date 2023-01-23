package dev.router.sisggarapi.adapter.request.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    @NotBlank(message = "Nif é obrigatório")
    private String nif;
    @NotBlank(message = "Endereço é obrigatório")
    private String address;
    @NotBlank(message = "Telefone é obrigatório")
    private String telephone;
    @NotBlank(message = "Email é obrigatório")
    private String email;
}
