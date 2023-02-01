package dev.router.sisggarapi.adapter.controller;

import dev.router.sisggarapi.adapter.mapper.StorageMapper;
import dev.router.sisggarapi.adapter.request.storage.StorageRequest;
import dev.router.sisggarapi.adapter.response.storage.StorageResponse;
import dev.router.sisggarapi.core.service.StorageService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Storage", description = "The Storage API. Contains all the operations that can be performed on a storage.")
public class StorageController {

    private final StorageService storageService;
    private final StorageMapper mapper;

    @Operation(summary = "Registar Armazem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registo efectuado com sucesso!", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StorageResponse> createStorage(@Valid @RequestBody StorageRequest storageRequest) {
        var storage = mapper.toStorage(storageRequest);
        var storageSaved = storageService.createStorage(storage);
        var storageResponse = mapper.toStorageResponse(storageSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(storageResponse);
    }


    @Operation(summary = "Actualizar dados do Armazem", description = "Actualização dos dados registados de um armazem de garrafas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados actualizados com sucesso!", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso do armazem não encontrado", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção de Servidor", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PutMapping("/{storageId}")
    public ResponseEntity<StorageResponse> updateStorage(@PathVariable(value = "storageId") UUID storageId,
                                                         @RequestBody @Valid StorageRequest request) {

        return Stream.of(request)
                .map(mapper::toStorage)
                .map(storage -> storageService.updateStorage(storage, storageId))
                .map(mapper::toStorageResponse)
                .map(pacienteResponse -> ResponseEntity.status(HttpStatus.OK).body(pacienteResponse))
                .findFirst()
                .get();
    }

    @Operation(summary = "Eliminar armazem", description = "Eliminar dados de registo de um armazem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Armazem eliminado com sucesso!", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso do armazem não encontrado", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção de Servidor", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @DeleteMapping("/{storageId}")
    public ResponseEntity<Object> deleteStorage(@PathVariable(value = "storageId") UUID storageId) {
        storageService.delete(storageId);
        return ResponseEntity.status(HttpStatus.OK).body("Storage deleted successfully.");
    }


    @Operation(summary = "Buscar todos os Armazens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A busca foi bem-sucedida", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso do armazem não encontrado", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção de Servidor", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping()
    public ResponseEntity<Page<StorageResponse>> getAllStorages(@RequestParam(required = false) String description,
                                                               @ParameterObject @PageableDefault(page = 0, size = 10, sort = "storage_id", direction = Sort.Direction.ASC) Pageable pageable) {

        List<StorageResponse> storageResponses = storageService.findAll(description)
                .stream()
                .map(mapper::toStorageResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > storageResponses.size() ? storageResponses.size()
                : (start + pageable.getPageSize()));

        Page<StorageResponse> storagesPageList = new PageImpl<StorageResponse>(storageResponses.subList(start, end), pageable, storageResponses.size());
        return ResponseEntity.status(HttpStatus.OK).body(storagesPageList);

    }

    @Operation(summary = "Buscar Armazem pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A busca foi bem-sucedida!", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Recurso não Encontrado", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = StorageRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping("/{storageId}")
    public ResponseEntity<StorageResponse> getOneStorage(@PathVariable(value = "storageId") UUID storageId) {
        return storageService.findById(storageId)
                .map(mapper::toStorageResponse)
                .map(storageResponse -> ResponseEntity.status(HttpStatus.OK).body(storageResponse))
                .orElse(ResponseEntity.notFound().build());
    }


}
