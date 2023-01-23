package dev.router.sisggarapi.adapter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<SisggarException.Field> fields = new ArrayList<>();
        var problema = new SisggarException();
        problema.setStatus(status.value());
        problema.setDataHora(LocalDateTime.now());
        problema.setTitulo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente");
        problema.setFilds(fields);

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            fields.add(new SisggarException.Field(fieldName, errorMessage));
        });
        return handleExceptionInternal(ex, problema, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        Error erros = new Error(ex.getMessage(), ExceptionUtils.getRootCauseMessage(ex));
        logger.error(erros, ex);
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Getter
    @Setter
    public static class Error {
        private String message;
        private String exceptionMessage;

        public Error(String message, String exceptionMessage) {
            this.message = message;
            this.exceptionMessage = exceptionMessage;
        }

        @Override
        public String toString() {
            return "Erro{" +
                    "message='" + message + '\'' +
                    ", exceptionMessage='" + exceptionMessage + '\'' +
                    '}';
        }
    }
}
