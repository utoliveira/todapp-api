package com.atlas.todappapi.factory;

import com.atlas.todappapi.consts.code.CodeEnumerable;
import com.atlas.todappapi.model.response.SimpleCodeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    public static ResponseEntity<?> getSimpleCodeResponse(HttpStatus status, CodeEnumerable enumerable ){
        return ResponseEntity.status(status)
                .body(SimpleCodeResponse.builder()
                        .code(enumerable.getCode())
                        .build());
    }
}
