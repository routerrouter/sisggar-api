package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.mapper.ProviderMapper;
import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.request.provider.ProviderRequest;
import dev.router.sisggarapi.adapter.response.provider.ProviderResponse;
import dev.router.sisggarapi.adapter.specifications.SpecificationTemplate;
import dev.router.sisggarapi.core.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/provider")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderMapper mapper;

    public ProviderController(ProviderService providerService, ProviderMapper mapper) {
        this.providerService = providerService;
        this.mapper = mapper;
    }

    @Operation(summary = "Cadastrar Fornecedor ", method = "POST", tags = {"provider"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = ProviderRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping()
    public ResponseEntity<ProviderResponse> createProvider(@RequestBody
                                                           @Valid @JsonView(ProviderRequest.ProviderView.RegistrationPost.class) ProviderRequest request) {

        return Stream.of(request)
                .map(mapper::toProvider)
                .map(providerService::createProvider)
                .map(mapper::toProviderResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .findFirst()
                .get();
    }

    @Operation(summary = "Actualizar Fornecedor", method = "PUT", tags = {"provider"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados actualizados com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = ProviderRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping("{providerId}")
    public ResponseEntity<ProviderResponse> updateProvider(@RequestBody
                                                           @Valid
                                                           @JsonView(ProviderRequest.ProviderView.ProviderPut.class) ProviderRequest request,
                                                           @Parameter(description = "ID passado para pesquisa") @PathVariable UUID providerId) {
        return Stream.of(request)
                .map(mapper::toProvider)
                .map(provider -> providerService.updateProvider(provider, providerId))
                .map(mapper::toProviderResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .findFirst()
                .get();
    }

    @Operation(summary = "Lista de Fornecedores", method = "GET", tags = {"provider"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = ProviderRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping()
    public ResponseEntity<Page<ProviderResponse>> getAllProviders(@RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) String nif,
                                                                  @RequestParam(required = false) String telephone,
                                                                  @ParameterObject @PageableDefault(page = 0, size = 10, sort = "providerId", direction = Sort.Direction.ASC) Pageable pageable) {
        var providerList = providerService.findAll(name, nif , telephone)
                .stream()
                .map(mapper::toProviderResponse)
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > providerList.size() ? providerList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > providerList.size() ? providerList.size()
                : (start + pageable.getPageSize()));
        Page<ProviderResponse> providerResponseList = new PageImpl<ProviderResponse>(providerList.subList(start, end), pageable, providerList.size());

        return ResponseEntity.status(HttpStatus.OK).body(providerResponseList);
    }

    @Operation(summary = "Busca pelo ID", method = "GET", tags = {"provider"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = ProviderRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/{providerId}")
    public ResponseEntity<ProviderResponse> getOneProvider(@PathVariable(value = "providerId") UUID providerId) {
        return providerService.findById(providerId)
                .map(mapper::toProviderResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar Status",  method = "PATCH", tags = {"provider"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status actualizado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = ProviderRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PatchMapping("/modifyStatus/{providerId}")
    public ResponseEntity<Object> modifyStatus(@RequestBody
                                               @JsonView(ProviderRequest.ProviderView.UpdateStatus.class)
                                                       ProviderRequest request,
                                               @Parameter(description = "ID passado para pesquisa") @PathVariable UUID providerId) {
        var provider = mapper.toProvider(request);
        providerService.updateStatus(provider, providerId);
        return ResponseEntity.status(HttpStatus.OK).body("Status modificado com Sucesso!");
    }
}
