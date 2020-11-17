package com.atlas.todappapi.bean.controller;

import com.atlas.todappapi.bean.service.RegisterService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.consts.code.OperationCode;
import com.atlas.todappapi.exception.EmailAlreadyRegisteredException;
import com.atlas.todappapi.model.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.atlas.todappapi.factory.ResponseFactory.getSimpleCodeResponse;

@RestController
@RequestMapping(TodappEndpoint.REGISTER)
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult validationResults){

        //TODO: change to an aspect based on methods with ResponseEntity and Validation Results
        if(validationResults.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(validationResults.getFieldErrors().stream()
                            .map(field -> field.getField() + ": " + field.getDefaultMessage()));
        }

        registerService.register(request);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({EmailAlreadyRegisteredException.class})
    private ResponseEntity<?> emailAlreadyUsedExceptionHandler(){
        return getSimpleCodeResponse(HttpStatus.PRECONDITION_FAILED, OperationCode.EMAIL_ALREADY_USED);
    }

}
