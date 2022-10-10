package ODL.web.authservice.controller;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ODL.web.authservice.dto.NamedExceptionDTO;
import ODL.web.authservice.exception.BusinessEntityNotFoundException;
import ODL.web.authservice.exception.BusinessException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<NamedExceptionDTO> handleInvalidFormatException(Exception e) {
        return getResponseEntity("I'm teapot", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
     * Handler for internal business exceptions
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<NamedExceptionDTO> getException(BusinessException e) {
        return getResponseEntity(e.getName(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /*
     * Handler for EntityNotFound flow exceptions
     */
    @ExceptionHandler(BusinessEntityNotFoundException.class)
    public ResponseEntity<NamedExceptionDTO> handleEntityNotFoundException(BusinessEntityNotFoundException ex) {
        return getResponseEntity(ex.getName(), ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /*
     * Handler for Spring validation and custom validator exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Set<NamedExceptionDTO>> handleValidationException(MethodArgumentNotValidException ex) {
        Set<NamedExceptionDTO> exceptions = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new NamedExceptionDTO(LocalDateTime.now(),
                        error.getObjectName() + "." + error.getField() + ".invalid", error.getDefaultMessage()))
                .collect(Collectors.toSet());
        return new ResponseEntity<>(exceptions, HttpStatus.BAD_REQUEST);
    }

    /*
     * Handler for DTO parse exceptions
     */
    @ExceptionHandler({ HttpMessageNotReadableException.class, NumberFormatException.class })
    public ResponseEntity<NamedExceptionDTO> handleHttpMessageNotReadableException(Exception ex) {
        return getResponseEntity("json.parse.exception", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<NamedExceptionDTO> getResponseEntity(String error, String message, HttpStatus status) {
        return new ResponseEntity<>(new NamedExceptionDTO(LocalDateTime.now(), status.value(), error, message), status);
    }

}