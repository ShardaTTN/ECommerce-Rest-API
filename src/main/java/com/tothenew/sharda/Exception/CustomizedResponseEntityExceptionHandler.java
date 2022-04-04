package com.tothenew.sharda.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<Object> handleTokenNotFoundException(InvalidTokenException e) {
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EmailAlreadyConfirmedException.class})
    public ResponseEntity<Object> handleEmailAlreadyConfirmedException(EmailAlreadyConfirmedException e) {
        return new ResponseEntity<>(e, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = {InvalidEmailException.class})
//    public ResponseEntity<Object> handleInvalidEmailException(InvalidEmailException e) {
//        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(value = {EmailAlreadyTakenException.class})
//    public ResponseEntity<Object> handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
//        return new ResponseEntity<>(e, HttpStatus.ALREADY_REPORTED);
//    }
}