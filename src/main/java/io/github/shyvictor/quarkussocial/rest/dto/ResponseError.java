package io.github.shyvictor.quarkussocial.rest.dto;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
public class ResponseError {
    private String message;
    private Collection<FieldError> errors;
    public ResponseError(String message, Collection<FieldError> errors){
        this.message = message;
        this.errors = errors;
    }
    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){
        final List<FieldError> errors = violations.stream().map(error -> new FieldError(error.getPropertyPath().toString(),
                error.getMessage())).collect(Collectors.toList());
        final ResponseError responseError = new ResponseError("Validation Error", errors);
        return responseError;

    }
}
