package dev.router.sisggarapi.adapter.controller;

import dev.router.sisggarapi.adapter.mapper.ExistenceMapper;
import dev.router.sisggarapi.adapter.request.costumer.CostumerRequest;
import dev.router.sisggarapi.adapter.request.existence.ExistenceRequest;
import dev.router.sisggarapi.adapter.response.existence.ExistenceResponse;
import dev.router.sisggarapi.core.domain.Existence;
import dev.router.sisggarapi.core.service.ExistenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/existence")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExistenceController {

    private final ExistenceService existenceService;
    private final ExistenceMapper mapper;

    @Operation(summary = "Existência no Estoque", method = "GET", tags = {"existence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping("/storage/{storageId}")
    public ResponseEntity<List<ExistenceResponse>> existences(@PathVariable("storageId") UUID storageId){
        List<Existence> existences = existenceService.findAll(storageId);
        List<ExistenceResponse> existenceResponses = mapper.toExistenceResponseList(existences);

        return ResponseEntity.status(HttpStatus.OK).body(existenceResponses);
    }

    @Operation(summary = "Transferencia de estoque", method = "GET", tags = {"existence"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação efetuada com sucesso!.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Erro na requisição! Verifique as informações enviadas.", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar este recurso", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Exceção do Servidor", content = @Content(schema = @Schema(implementation = CostumerRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PutMapping("storageOrigin/{storageId}/transfer")
    public ResponseEntity<Object> transferBetweenStorages(
            @PathVariable("storageId") UUID storageId,
            @Valid @RequestBody ExistenceRequest request){
        existenceService.transferBetweenStorage(storageId,request);

        return ResponseEntity.status(HttpStatus.OK).body("Transferencia efetuada com sucesso!");
    }
}
