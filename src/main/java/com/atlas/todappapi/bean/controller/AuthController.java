package com.atlas.todappapi.bean.controller;

import com.atlas.todappapi.bean.service.AuthService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.exception.PasswordDoesntMatchException;
import com.atlas.todappapi.exception.UserNotFoundException;
import com.atlas.todappapi.model.request.AuthRequest;
import com.atlas.todappapi.model.response.AuthResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(TodappEndpoint.AUTH)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity auth(@Valid @RequestBody AuthRequest authRequest, BindingResult validationResults){

        //TODO: change to an aspect based on methods with ResponseEntity and Validation Results
        if(validationResults.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(validationResults.getFieldErrors().stream()
                            .map(field -> field.getField() + ": " + field.getDefaultMessage()));
        }

        return ResponseEntity.ok(
                AuthResponse.builder()
                .token(authService.auth(authRequest))
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity userNotFoundExceptionHandler(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PasswordDoesntMatchException.class)
    public ResponseEntity passwordDoesntMatchExceptionHandler(){
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
    }
}
