package footstep.footstep.configs.security;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import footstep.footstep.dtos.exception.ExceptionDTO;

@RestControllerAdvice
public class ControllerExceptionError {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateEntry (DataIntegrityViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "400");

        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}
