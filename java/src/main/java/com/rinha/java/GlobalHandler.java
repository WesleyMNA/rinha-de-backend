package com.rinha.java;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseEntity<Void>> handleGlobalException(Exception ex, WebRequest request) {
        if (ex instanceof SQLException || ex instanceof IllegalArgumentException)
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();

        return ResponseEntity
                .badRequest()
                .build();
    }
}
