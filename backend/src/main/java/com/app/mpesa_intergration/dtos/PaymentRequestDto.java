package com.app.mpesa_intergration.dtos;

import io.micrometer.common.lang.NonNull;

public class PaymentRequestDto {
    @NonNull
    private String phoneNumber;

    @NonNull
    private String amount;

    public PaymentRequestDto(String phoneNumber,String amount){
        this.phoneNumber =phoneNumber;
        this.amount = amount;
    }
    public PaymentRequestDto(){}

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public String getAmount(){
        return this.amount;
    }
    public void setAmount(String amount){
        this.amount =  amount;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

}

