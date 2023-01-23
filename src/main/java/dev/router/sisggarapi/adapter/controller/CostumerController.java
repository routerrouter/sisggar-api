package dev.router.sisggarapi.adapter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.mapper.CostumerMapper;
import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.response.costumer.CostumerResponse;
import dev.router.sisggarapi.core.service.CostumerService;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/costumer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CostumerController {

    private final CostumerService costumerService;
    private final CostumerMapper mapper;


    @Operation(summary = "Registar Cliente", method = "POST", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping(path = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CostumerResponse> createCostumer(@Valid
                                                           @RequestBody
                                                           @JsonView(CostumerRequest.CostumerView.RegistrationPost.class)
                                                                   CostumerRequest request) {

        return Stream.of(request)
                .map(mapper::toCostumer)
                .map(costumerService::createCostumer)
                .map(mapper::toCostumerResponse)
                .map(costumerResponse -> ResponseEntity.status(HttpStatus.CREATED).body(costumerResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Actualizar Cliente", method = "PUT", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo actualizada com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PutMapping(path = "/{costumerId}")
    public ResponseEntity<CostumerResponse> updateCostumer(@PathVariable("costumerId") UUID costumerId,
                                                           @Valid @RequestBody
                                                           @JsonView(CostumerRequest.CostumerView.CostumerPut.class) CostumerRequest request) {

        return Stream.of(request)
                .map(mapper::toCostumer)
                .map(costumer -> costumerService.updateCostumer(costumer, costumerId))
                .map(mapper::toCostumerResponse)
                .map(costumerResponse -> ResponseEntity.status(HttpStatus.OK).body(costumerResponse))
                .findFirst()
                .get();

    }

    @Operation(summary = "Listagem de todos Clientes", method = "GET", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping("/findAllBy/{storageId}")
    public ResponseEntity<Page<CostumerResponse>> getAll(@PathVariable("storageId") UUID storageId,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String nif,
                                                         @RequestParam(required = false) String telephone,
                                                         @RequestParam(required = false) UUID locationId,
                                                         @ParameterObject @PageableDefault(page = 0, size = 10, sort = "costumer_id", direction = Sort.Direction.ASC) Pageable pageable) {
        var costumersList = costumerService.findAll(name, nif, telephone, locationId, storageId)
                .stream()
                .map(mapper::toCostumerResponse)
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > costumersList.size() ? costumersList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > costumersList.size() ? costumersList.size()
                : (start + pageable.getPageSize()));
        Page<CostumerResponse> costumerDtoPageList = new PageImpl<CostumerResponse>(costumersList.subList(start, end), pageable, costumersList.size());

        return ResponseEntity.status(HttpStatus.OK).body(costumerDtoPageList);

    }

    @Operation(summary = "Buscar Cliente pelo código", method = "GET", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A busca foi bem-sucedida!",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor",
                    content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping("/{costumerId}")
    public ResponseEntity<CostumerResponse> findOne(@Parameter(description = "ID para Pesquisa") @PathVariable UUID costumerId) {
        return costumerService.findById(costumerId)
                .map(mapper::toCostumerResponse)
                .map(costumerResponse -> ResponseEntity.status(HttpStatus.OK).body(costumerResponse))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar limite de Entrega", method = "PATCH", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo actualizado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PatchMapping("/updateLimit/{costumerId}")
    public ResponseEntity<Object> updateLimit(
            @PathVariable("costumerId") UUID costumerId,
            @Valid @RequestBody @JsonView(CostumerRequest.CostumerView.UpdateLimit.class) CostumerRequest request) {

        var costumer = mapper.toCostumer(request);
        costumerService.updateLimit(costumer, costumerId);
        return ResponseEntity.status(HttpStatus.OK).body("Limite de entrega actualizado com sucesso!");
    }

    @Operation(summary = "Actualizar Posse", method = "PATCH", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo actualizado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PatchMapping("/updatePossession/{costumerId}")
    public ResponseEntity<Object> updatePossession(
            @PathVariable("costumerId") UUID costumerId,
            @RequestBody @JsonView(CostumerRequest.CostumerView.UpdatePossession.class) CostumerRequest request) {
        var costumer = mapper.toCostumer(request);
        costumerService.updatePossession(costumer, costumerId);
        return ResponseEntity.status(HttpStatus.OK).body("Posse de entrega actualizado com sucesso!");
    }

    @Operation(summary = "Deletar/Ativar Cliente", method = "PATCH", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo actualizado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PatchMapping("/updateStatus/{costumerId}")
    public ResponseEntity<Object> modifyStatus(
            @PathVariable("costumerId") UUID costumerId,
            @Valid @RequestBody @JsonView(CostumerRequest.CostumerView.UpdateStatus.class) CostumerRequest request) {
        var costumer = mapper.toCostumer(request);
        costumerService.modifyStatus(costumer, costumerId);
        return ResponseEntity.status(HttpStatus.OK).body("Status actualizado com sucesso!");
    }


    @Operation(summary = "Desassociar Gerente do Cliente", method = "DELETE", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação eliminada com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @DeleteMapping("/{costumerId}/costumer/{deliveryManagerId}/delivermanager")
    public ResponseEntity<Object> removeDeliveryManager(@PathVariable UUID costumerId,
                                                        @PathVariable UUID deliveryManagerId) {
        costumerService.removeCostumerIntoDeliveryManager(costumerId, deliveryManagerId);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente desvinculado do Gestor");
    }

    @Operation(summary = "Desassociar Localidade do Cliente", method = "DELETE", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação eliminada com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @DeleteMapping("/{costumerId}/costumer/{locationId}/location")
    public ResponseEntity<Object> removeCostumerIntoLocation(@PathVariable UUID costumerId,
                                                             @PathVariable UUID locationId) {
        costumerService.removeCostumerIntoLocation(costumerId, locationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Cliente desvinculado da Localidade");
    }


    @Operation(summary = "Associar Cliente a Localidade", method = "POST", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @PostMapping("/addLocationIntoCostumer/{costumerId}")
    public ResponseEntity<Object> addLocationIntoCostumer(@PathVariable("costumerId") UUID costumerId,
                                                          @JsonView(CostumerRequest.CostumerView.LocationsAdd.class)
                                                          @RequestBody @Valid CostumerRequest request) {
        costumerService.addIntoLocation(costumerId, request.getLocations());
        return ResponseEntity.status(HttpStatus.OK).body("Cliente associado a(s) localidade(s)");
    }

    @Operation(summary = "Associar Gerente ao Cliente", method = "POST", tags = {"costumer"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @PostMapping("/addDeliveryManagerIntoCostumer/{costumerId}")
    public ResponseEntity<Object> addDeliveryManagerIntoCostumer(@PathVariable("costumerId") UUID costumerId,
                                                                 @RequestBody @Valid CostumerRequest request) {
        costumerService.addIntoDeliveryManager(costumerId, request.getDeliveryManagers());
        return ResponseEntity.status(HttpStatus.OK).body("Cliente Associado ao Gestor");
    }

}
