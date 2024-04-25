package tarasov.victor.taskmanager.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tarasov.victor.taskmanager.dto.ErrorResponseDto;
import tarasov.victor.taskmanager.exception.TaskManagerException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskManagerException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskManagerException(TaskManagerException exception) {
        var error = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception) {
        var error = new ErrorResponseDto(exception.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var allErrors = ex.getBindingResult().getAllErrors();
        var errorsWithMessages = allErrors.stream().filter(v -> v.getDefaultMessage() != null);
        var errorsInfo = errorsWithMessages.collect(Collectors.toMap(v -> ((FieldError) v).getField(),
                ObjectError::getDefaultMessage, (a, b) -> a + ", " + b));
        return new ResponseEntity<>(errorsInfo, BAD_REQUEST);
    }

}
