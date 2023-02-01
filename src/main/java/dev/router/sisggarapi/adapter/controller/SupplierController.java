package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.mapper.SupplierMapper;
import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.request.supplier.SupplierRequest;
import dev.router.sisggarapi.adapter.response.supplier.SupplierResponse;
import dev.router.sisggarapi.core.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Supplier", description = "The Supplier API. Contains all the operations that can be performed on a supplier.")
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierMapper mapper;

    @Operation(summary = "Cadastrar Fornecedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping()
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody
                                                           @Valid @JsonView(SupplierRequest.SupplierView.RegistrationPost.class) SupplierRequest request) {

        return Stream.of(request)
                .map(mapper::toSupplier)
                .map(supplierService::createSupplier)
                .map(mapper::toSupplierResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .findFirst()
                .get();
    }

    @Operation(summary = "Actualizar Fornecedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados actualizados com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping("{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(@RequestBody
                                                           @Valid
                                                           @JsonView(SupplierRequest.SupplierView.SupplierPut.class) SupplierRequest request,
                                                           @Parameter(description = "ID passado para pesquisa") @PathVariable UUID supplierId) {
        return Stream.of(request)
                .map(mapper::toSupplier)
                .map(supplier -> supplierService.updateSupplier(supplier, supplierId))
                .map(mapper::toSupplierResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .findFirst()
                .get();
    }

    @Operation(summary = "Lista de Fornecedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping()
    public ResponseEntity<Page<SupplierResponse>> getAllSuppliers(@RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) String nif,
                                                                  @RequestParam(required = false) String telephone,
                                                                  @ParameterObject @PageableDefault(page = 0, size = 10, sort = "SupplierId", direction = Sort.Direction.ASC) Pageable pageable) {
        var supplierList = supplierService.findAll(name, nif, telephone)
                .stream()
                .map(mapper::toSupplierResponse)
                .collect(Collectors.toList());

        int start = (int) (pageable.getOffset() > supplierList.size() ? supplierList.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > supplierList.size() ? supplierList.size()
                : (start + pageable.getPageSize()));
        Page<SupplierResponse> supplierResponseList = new PageImpl<SupplierResponse>(supplierList.subList(start, end), pageable, supplierList.size());

        return ResponseEntity.status(HttpStatus.OK).body(supplierResponseList);
    }

    @Operation(summary = "Busca pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> getOneSupplier(@PathVariable(value = "supplierId") UUID supplierId) {
        return supplierService.findById(supplierId)
                .map(mapper::toSupplierResponse)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status actualizado com sucesso!", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PatchMapping("/modifyStatus/{supplierId}")
    public ResponseEntity<Object> modifyStatus(@RequestBody
                                               @JsonView(SupplierRequest.SupplierView.UpdateStatus.class)
                                                       SupplierRequest request,
                                               @Parameter(description = "ID passado para pesquisa") @PathVariable UUID supplierId) {
        var supplier = mapper.toSupplier(request);
        supplierService.updateStatus(supplier, supplierId);
        return ResponseEntity.status(HttpStatus.OK).body("Status modificado com Sucesso!");
    }
}
