package com.app.mpesa_intergration.dtos;

import io.micrometer.common.lang.NonNull;

public class TransactionStatusDto {
    
    @NonNull
    private String checkOutRequestId;
    
    public TransactionStatusDto(String checkOutRequestId){
        this.checkOutRequestId = checkOutRequestId;
    }
    public TransactionStatusDto(){}

    public void setCheckOutRequestId(String checkOutRequestId){
        this.checkOutRequestId = checkOutRequestId;
    }

    public String  getCheckOutRequestId(){
        return this.checkOutRequestId;
    }
}
