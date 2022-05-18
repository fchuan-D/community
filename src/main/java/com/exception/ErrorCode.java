package com.exception;

public enum ErrorCode implements IErrorCode{

    QUESTION_NOT_CREATE("问题发布失败,重新试试?"),

    QUESTION_NOT_FOUND("你找的问题不存在,换个试试?"),

    PAGE_NOT_FOUND("该页码不存在,请重试!"),

    QUESTION_NOT_UPDATE("问题修改失败,重新试试?");

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    ErrorCode(String message) {
        this.message = message;
    }
}
