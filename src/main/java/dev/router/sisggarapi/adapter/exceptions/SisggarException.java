package dev.router.sisggarapi.adapter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SisggarException {

    private Integer status;
    private LocalDateTime dataHora;
    private String titulo;
    private List<Field> filds;

    @AllArgsConstructor
    @Getter
    public static class Field {
        private String name;
        private String message;
    }
}
