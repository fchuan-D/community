// @author:樊川
// @email:945001786@qq.com
package com.exception;

public class CustomizeException extends RuntimeException{
    private Integer code;
    private String message;

    public CustomizeException(IErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
