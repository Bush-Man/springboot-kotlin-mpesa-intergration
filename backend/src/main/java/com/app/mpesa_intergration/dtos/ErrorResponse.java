package com.app.mpesa_intergration.dtos;

public class ErrorResponse extends ApiResBaseClass{
    private String errorType;
    private String errorMessage;

    public ErrorResponse(String errorType, String errorMessage,String status,String message) {
        this.errorType = errorType;
        this.errorMessage= errorMessage;
        this.setStatus(status);
        this.setMessage(message);
    }
    public ErrorResponse(){
        super();
    }

    public String getErrorType() {
        return  errorType;
    }

    public void setErrorType(String  errorType) {
        this.errorType =  errorType;
    }

    public String getErrorMessage() {
        return  errorMessage;
    }

    public void setErrorMessage(String  errorMessage) {
        this. errorMessage =  errorMessage;
    }
} 
    

