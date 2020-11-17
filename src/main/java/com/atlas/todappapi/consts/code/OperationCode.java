package com.atlas.todappapi.consts.code;

import lombok.Getter;

public enum OperationCode implements CodeEnumerable{

    LOGIN_ALREADY_USED("REGERR1"),
    EMAIL_ALREADY_USED("REGERR2");

    private OperationCode(String code){
        this.code = code;
    }

    @Getter
    private String code;
}
