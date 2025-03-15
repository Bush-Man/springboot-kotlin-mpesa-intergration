package com.app.mpesa_intergration.controller;

import org.springframework.web.bind.annotation.RestController;

import com.app.mpesa_intergration.dtos.ApiResBaseClass;
import com.app.mpesa_intergration.dtos.PaymentRequestDto;
import com.app.mpesa_intergration.dtos.TransactionStatusDto;
import com.app.mpesa_intergration.services.MpesaService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
public class MpesaController {

    @Autowired
    MpesaService mpesaService;

   
    @PostMapping("/pay")
    public ResponseEntity<ApiResBaseClass> paymentRequest(@RequestBody PaymentRequestDto paymentInfo) {
        return mpesaService.paymentRequest(paymentInfo);
    }
    
    
    // @PostMapping("callback/{uniqueKey}")
    // public void handleCallBack(@PathVariable(value = "uniqueKey") String uniqueKey, @RequestBody Map<String,Object> callBackData) {

        
    //     System.out.println(callBackData);
    //     System.out.println(uniqueKey);
    // }
    @PostMapping("/status")
    public ResponseEntity<ApiResBaseClass> stkQueryStatus(@RequestBody TransactionStatusDto requestId) {
        String  checkOutRequestId = requestId.getCheckOutRequestId();
        return mpesaService.transactionStatus(checkOutRequestId);
    }
    
    
}
