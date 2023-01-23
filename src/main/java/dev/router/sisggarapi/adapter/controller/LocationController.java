package dev.router.sisggarapi.adapter.controller;

import dev.router.sisggarapi.adapter.mapper.LocationMapper;
import dev.router.sisggarapi.adapter.request.location.LocationRequest;
import dev.router.sisggarapi.adapter.response.location.LocationResponse;
import dev.router.sisggarapi.core.service.LocationService;
import dev.router.sisggarapi.core.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LocationController {


    private final LocationService locationService;
    private final StorageService storageService;
    private final LocationMapper mapper;


    @Operation(summary = "Desativar Localidade", method = "DELETE", tags = {"location"})
    @DeleteMapping("/{locationId}")
    public ResponseEntity<Object> deleteStorage(@PathVariable(value = "locationId") UUID locationId) {
        locationService.delete(locationId);
        return ResponseEntity.status(HttpStatus.OK).body("Localização eliminada com sucesso!");
    }

    @Operation(summary = "Buscar localidades de um Armazem", method = "GET", tags = {"location"})
    @GetMapping("/storage/{storageId}/locations")
    public ResponseEntity<Page<LocationResponse>> getLocationsIntoStorage(@Parameter(description = "ID do armazem passado")
                                                                              @PathVariable UUID storageId,
                                                                          //SpecificationTemplate.LocationSpec spec,
                                                                          @ParameterObject
                                                                          @PageableDefault(page = 0, size = 10, sort = "locationId", direction = Sort.Direction.ASC) Pageable pageable) {

        List<LocationResponse> responseList = locationService.getLocationsIntoStorage(pageable)
                .stream()
                .map(mapper::toLocationResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > responseList.size() ? responseList.size()
                : (start + pageable.getPageSize()));

        Page<LocationResponse> locationResponsePage = new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());

        return ResponseEntity.status(HttpStatus.OK).body(locationResponsePage);

    }

    @Operation(summary = "Buscar localidade pelo ID", method = "GET", tags = {"location"})
    @GetMapping("/{locationId}")
    public ResponseEntity<LocationResponse> getLocation(
            @Parameter(description = "ID da localidade a pesquisar")
            @PathVariable(value = "locationId") UUID locationId) {

        return locationService.findById(locationId)
                .map(mapper::toLocationResponse)
                .map(toLocationResponse -> ResponseEntity.status(HttpStatus.OK).body(toLocationResponse))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar nova Localidade", method = "POST", tags = {"location"})
    @PostMapping()
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest request) {

        return Stream.of(request)
                .map(mapper::toLocation)
                .map(locationService::createLocation)
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.CREATED).body(locationResponse))
                .findFirst().get();
    }

    @Operation(summary = "Alterar Localidade", method = "PUT", tags = {"location"})
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable(value = "locationId") UUID locationId,
                                                 @RequestBody @Valid LocationRequest request) {

        return Stream.of(request)
                .map(mapper::toLocation)
                .map(location -> locationService.updateLocation(location,locationId))
                .map(mapper::toLocationResponse)
                .map(locationResponse -> ResponseEntity.status(HttpStatus.OK).body(locationResponse))
                .findFirst()
                .get();

    }

}
