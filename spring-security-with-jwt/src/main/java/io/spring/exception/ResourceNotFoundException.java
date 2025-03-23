package io.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resource;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(String resource, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resource, fieldName, fieldValue));
        this.resource = resource;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResource() {
        return resource;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
