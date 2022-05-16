package com.example.spring_rest_app.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//The @ExceptionHandler annotated method is only active for that particular Controller, not globally for the entire application.
// Of course, adding this to every controller makes it not well suited for a general exception handling mechanism.

//Permet de gérer les exceptions d'une manière globale
@ControllerAdvice
public class RestAppExceptionHandler  {
    @ExceptionHandler
    public ResponseEntity<PersonError> handleException(Exception ex) {
        //creer et initialiszr l'object personError qui decrit l'exception
        PersonError err=new PersonError();
        err.setCodeStatut(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setDescription("Une erreur interne est survenue :" + ex.getMessage());
        return new ResponseEntity<PersonError>(err, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
