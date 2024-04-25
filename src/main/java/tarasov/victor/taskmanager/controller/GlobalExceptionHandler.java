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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tarasov.victor.taskmanager.dto.ErrorResponseDto;
import tarasov.victor.taskmanager.exception.TaskManagerException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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
        Function<ObjectError, String> toFieldName = v -> ((FieldError) v).getField();
        Function<ObjectError, String> toMessage = ObjectError::getDefaultMessage;
        Predicate<ObjectError> withMessage = v -> v.getDefaultMessage() != null;
        var allErrors = ex.getBindingResult().getAllErrors();
        var errorsInfo = allErrors.stream().filter(withMessage).collect(Collectors.toMap(toFieldName, toMessage, (a, b) -> a + ", " + b));
        return new ResponseEntity<>(errorsInfo, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var message = "The parameter " + ex.getName() + " of value '" + ex.getValue() + "' could not be converted to type "
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        var error = new ErrorResponseDto(message, LocalDateTime.now());
        return new ResponseEntity<>(error, BAD_REQUEST);

    }

}
