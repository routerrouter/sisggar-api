package dev.router.sisggarapi.adapter.controller;

import dev.router.sisggarapi.adapter.mapper.CompanyMapper;
import dev.router.sisggarapi.adapter.request.company.CompanyImageRequest;
import dev.router.sisggarapi.adapter.request.company.CompanyRequest;
import dev.router.sisggarapi.adapter.response.company.CompanyResponse;
import dev.router.sisggarapi.core.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Company", description = "The Company API. Contains all the operations that can be performed on a company.")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper mapper;


    @Operation(summary = "Registar Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CompanyRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CompanyRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @PostMapping()
    public ResponseEntity<CompanyResponse> updateCompany(@Validated @RequestBody CompanyRequest request) {
        var company = mapper.toCompany(request);
        log.debug("POST updateCompany request received {} ", request.toString());
        var companySaved = companyService.updateOrSaveCompanyInfo(company);
        log.debug("PUT updateCompany companyId saved {} ", companySaved.getCompanyId());
        log.info("Info updated successfully companyId {} ", companySaved.getCompanyId());
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toCompanyResponse(company));
    }

    @Operation(summary = "Actuaizar Logotipo/imagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro da empresa actualizado com sucesso", content = @Content(schema = @Schema(implementation = CompanyImageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso da empresa não encontrado", content = @Content(schema = @Schema(implementation = CompanyImageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CompanyImageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @PatchMapping("/updateImage/{companyId}")
    public ResponseEntity<Object> updateUrlImage(@PathVariable(name = "companyId") UUID companyId,
            @Valid @RequestBody CompanyImageRequest imageRequest) {
        var company = mapper.toCompany(imageRequest);
        log.debug("POST updateCompany imageRequest received {} ", imageRequest.toString());
        var urlImage = companyService.updateUrlImage(company, companyId);
        return ResponseEntity.status(HttpStatus.OK).body(urlImage);
    }

    @Operation(summary = "Detalhes da Empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A busca foi bem sucedida", content = @Content(schema = @Schema(implementation = CompanyResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso da empresa não encontrado", content = @Content(schema = @Schema(implementation = CompanyResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção de servidor", content = @Content(schema = @Schema(implementation = CompanyResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @GetMapping("companyDetails")
    public ResponseEntity<Object> companyDetails(){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyDetails());
    }
}
