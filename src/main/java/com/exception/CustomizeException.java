// @author:樊川
// @email:945001786@qq.com
package com.exception;

public class CustomizeException extends RuntimeException{
    private String message;

    public CustomizeException(IErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
