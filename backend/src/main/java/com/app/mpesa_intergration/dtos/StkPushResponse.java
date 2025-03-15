package com.app.mpesa_intergration.dtos;

import javax.print.DocFlavor.STRING;

import org.springframework.boot.context.properties.bind.DefaultValue;

import io.micrometer.common.lang.Nullable;

public class StkPushResponse extends ApiResBaseClass{
    private String merchantRequestID;
    private String checkoutRequestID;
    private String responseCode;
    private String responseDescription;
    private String customerMessage;
    @Nullable
    private String resultDesc;
    

    public StkPushResponse() {
        super();
    }

    public StkPushResponse(String merchantRequestID, String checkoutRequestID, String responseCode, String responseDescription, String customerMessage,String resultDesc,String message, String status) {
        this.merchantRequestID = merchantRequestID;
        this.checkoutRequestID = checkoutRequestID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.customerMessage = customerMessage;
        this.resultDesc = resultDesc;
        this.setStatus(status);
        this.setMessage(message);
    
    }

    public String getMerchantRequestID() {
        return merchantRequestID;
    }

    public void setMerchantRequestID(String merchantRequestID) {
        this.merchantRequestID = merchantRequestID;
    }

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        this.checkoutRequestID = checkoutRequestID;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }


    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

  
}