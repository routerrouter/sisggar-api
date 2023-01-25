package dev.router.sisggarapi.adapter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.mapper.DeliveryManagerMapper;
import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.request.deliverManager.DeliveryManagerRequest;
import dev.router.sisggarapi.adapter.response.deliverManager.DeliveryManagerResponse;
import dev.router.sisggarapi.core.service.DeliveryManagerService;
import dev.router.sisggarapi.core.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveryManager")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeliveryManagerController {


    private final DeliveryManagerService managerService;
    private final StorageService storageService;
    private final DeliveryManagerMapper mapper;


    @Operation(summary = "Cadastrar gestor de Entregas", method = "DELETE", tags = {"delivery-manager"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping()
    public ResponseEntity<DeliveryManagerResponse> createDeliveryManager(@Valid @RequestBody @JsonView(DeliveryManagerRequest.ManagerView.RegistrationPost.class) DeliveryManagerRequest request) {
        Optional<DeliveryManagerResponse> managerResponse = Stream.of(request)
                .map(mapper::toDeliveryManager)
                .map(managerService::createDeliveryManager)
                .map(mapper::toDeliveryManagerResponse)
                .findFirst();
        return ResponseEntity.status(HttpStatus.CREATED).body(managerResponse.get());
    }

    @Operation(summary = "Actualizar dados do gestor de Entregas", method = "DELETE", tags = {"delivery-manager"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo alterado com sucesso!", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @PutMapping("/{deliveryManagerId}")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(@Parameter(description = "ID de pesquisa de um gestor") @PathVariable(value = "deliveryManagerId") UUID deliveryManagerId,
                                                                         @RequestBody @Valid @JsonView(DeliveryManagerRequest.ManagerView.ManagerPut.class)
                                                                                 DeliveryManagerRequest request) {

        return Stream.of(request)
                .map(mapper::toDeliveryManager)
                .map(manager -> managerService.updateDeliveryManager(manager, deliveryManagerId))
                .map(mapper::toDeliveryManagerResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .findFirst()
                .get();
    }

    @Operation(summary = "Listar Gestores", method = "GET", tags = {"delivery-manager"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping("/{storageId}/storage")
    public ResponseEntity<Page<DeliveryManagerResponse>> getAllDeliveryManagers(@Parameter(description = "ID do armazem") @PathVariable UUID storageId,
                                                                                @RequestParam(required = false) String fullName,
                                                                                @ParameterObject @PageableDefault(page = 0, size = 10, sort = "delivery_manager_id",
                                                                                        direction = Sort.Direction.ASC) Pageable pageable) {
        var deliveryManagersList = managerService.findAll(storageId, fullName)
                .stream()
                .map(mapper::toDeliveryManagerResponse)
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > deliveryManagersList.size() ? deliveryManagersList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > deliveryManagersList.size() ? deliveryManagersList.size()
                : (start + pageable.getPageSize()));
        int sizeList = deliveryManagersList.size();
        Page<DeliveryManagerResponse> responsePage = new PageImpl<>(deliveryManagersList.subList(start, end), pageable, sizeList);

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);

    }

    @Operation(summary = "Pesquisar gestor pelo ID", method = "DELETE", tags = {"delivery-manager"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping("/{deliveryManagerId}")
    public ResponseEntity<DeliveryManagerResponse> getOneDeliveryManager(@PathVariable(value = "deliveryManagerId") UUID deliveryManagerId) {
        return managerService.findById(deliveryManagerId)
                .map(mapper::toDeliveryManagerResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Desativar/Excluir gestor de Entregas", method = "DELETE", tags = {"delivery-manager"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = DeliveryManagerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @DeleteMapping("/{deliveryManagerId}")
    public ResponseEntity<Object> deleteDeliveryManager(@PathVariable(value = "deliveryManagerId") UUID deliveryManagerId) {
        managerService.delete(deliveryManagerId);
        return ResponseEntity.status(HttpStatus.OK).body("DeliveryManager deleted successfully.");
    }


}
