package dev.router.sisggarapi.adapter.mapper;

import dev.router.sisggarapi.adapter.request.location.LocationRequest;
import dev.router.sisggarapi.adapter.response.location.LocationResponse;
import dev.router.sisggarapi.core.domain.Location;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LocationMapper {
    
    private final ModelMapper mapper;

    public Location toLocation(LocationRequest request){
        return mapper.map(request, Location.class);
    }

    public LocationResponse toLocationResponse(Location Location) {
        return mapper.map(Location, LocationResponse.class);
    }

    public List<LocationResponse> toLocationResponseList(List<Location> locations) {
        return locations.stream()
                .map(this::toLocationResponse)
                .collect(Collectors.toList());
    }
}
