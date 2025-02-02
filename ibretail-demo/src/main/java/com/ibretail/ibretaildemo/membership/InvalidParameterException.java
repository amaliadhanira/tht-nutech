package com.ibretail.ibretaildemo.membership;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidParameterException {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();

        // Ambil pesan error dari field tertentu
        String message = "Parameter " + fieldError.getField() + " tidak sesuai format";

        // Buat response yang singkat
        return new ResponseEntity<>(new ErrorResponse(102, message, null), HttpStatus.BAD_REQUEST);
    }
}
