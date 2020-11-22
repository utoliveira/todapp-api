package com.atlas.todappapi.consts;

public class TodappRequestConsts {

    public static final String EMAIL_REGEX = "^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+(\\.[a-z]+)?$";
    public static final int EMAIL_MAX_LENGTH = 100;

    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int PASSWORD_MIN_LENGTH=1;

    public static final int TODO_TEXT_MAX_LENGTH=255;
    public static final int TODO_TEXT_MIN_LENGTH=1;
}
