package com.solace.asyncapi.asyncapi_parser;

public class AsyncAPIToCICDException extends Exception {

    /**
     * errorCode == 0 can be ignored
     * errorCode  > 0 may be ignored
     * errorCode  < 0 is a failure
     */
    private Integer errorCode = 0;

    public AsyncAPIToCICDException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }
}