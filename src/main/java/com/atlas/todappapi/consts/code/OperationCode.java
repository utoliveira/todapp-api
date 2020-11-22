package com.atlas.todappapi.consts.code;

import lombok.Getter;

public enum OperationCode implements CodeEnumerable{

    LOGIN_ALREADY_USED("REGERR1"),
    EMAIL_ALREADY_USED("REGERR2"),
    USER_NOT_FOUND("AUTHERR1"),
    PASSWORD_DOESNT_MATCH("AUTHERR2"),
    DONT_HAVE_PERMISSION_FOR_THAT("PERM1");

    private OperationCode(String code){
        this.code = code;
    }

    @Getter
    private String code;
}
