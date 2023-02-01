package dev.router.sisggarapi.adapter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.config.secutity.UserDetailsImpl;
import dev.router.sisggarapi.adapter.mapper.SupplierMovementMapper;
import dev.router.sisggarapi.adapter.request.supplierMovement.MovementFindRequest;
import dev.router.sisggarapi.adapter.request.supplierMovement.SupplierMovementRequest;
import dev.router.sisggarapi.adapter.response.productMovement.SupplierMovementResponse;
import dev.router.sisggarapi.core.domain.SupplierMovement;
import dev.router.sisggarapi.core.service.SupplierMovementService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliermovement")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "SupplierMovement", description = "The Supplier Movement API. Contains all the operations that can be performed on a movement.")
public class SupplierMovementController {

    private final SupplierMovementService supplierMovementService;
    private final SupplierMovementMapper mapper;

    @Operation(summary = "Salvar movimentos de entrada e saidas do fornecedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!",
                    content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor",
                    content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping("/storage/{storageId}")
    public ResponseEntity<SupplierMovementResponse> saveMovement(@RequestParam() UUID storageId,
                                                                 @RequestBody
                                                                 @Valid @JsonView(SupplierMovementRequest.SupplierMovementView.RegistrationPost.class)
                                                                         SupplierMovementRequest request,
                                                                 Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<SupplierMovementResponse> response = Stream.of(request)
                .map(mapper::toSupplierMovement)
                .map(movement -> supplierMovementService.createSupplierMovement(storageId, movement, userDetails.getUserId()))
                .map(mapper::toSupplierMovementResponse)
                .findFirst();
        return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
    }

    @Operation(summary = "Listar movimentos de entrada e saidas de garrafas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping("/storage/{storageId}/movements")
    public ResponseEntity<Page<SupplierMovementResponse>> findAll(@PathVariable() UUID storageId,
                                                                  @RequestBody MovementFindRequest findRequest,
                                                                  @ParameterObject @PageableDefault(page = 0, size = 10, sort = "supplier_movement_id", direction = Sort.Direction.ASC) Pageable pageable) {

        List<SupplierMovement> movements = supplierMovementService.findAll(storageId, findRequest);
        List<SupplierMovementResponse> movementResponses = mapper.toSupplierMovementResponseList(movements);

        int start = (int) (pageable.getOffset() > movementResponses.size() ? movementResponses.size() : pageable.getOffset());
        int end = (int) ((start + pageable.getPageSize()) > movementResponses.size() ? movementResponses.size()
                : (start + pageable.getPageSize()));
        int sizeList = movementResponses.size();

        Page<SupplierMovementResponse> movementModelDtoPage = new PageImpl<>(movementResponses.subList(start, end), pageable, sizeList);
        return ResponseEntity.status(HttpStatus.OK).body(movementModelDtoPage);
    }


    @Operation(summary = "Eliminar movimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!",
                    content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor",
                    content = @Content(schema = @Schema(implementation = SupplierMovementRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))

    })
    @PatchMapping("/{movementId}/delete")
    public ResponseEntity<Object> deleteMovement(@PathVariable UUID movementId) {
        supplierMovementService.deleteSupplierMovement(movementId);
        return ResponseEntity.status(HttpStatus.OK).body("Movimento elimanado com sucesso!");
    }
}
