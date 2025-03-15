package com.app.mpesa_intergration.services;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.app.mpesa_intergration.controller.MpesaController;
import com.app.mpesa_intergration.dtos.ApiResBaseClass;
import com.app.mpesa_intergration.dtos.ErrorResponse;
import com.app.mpesa_intergration.dtos.PaymentRequestDto;
import com.app.mpesa_intergration.dtos.StkPushResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class MpesaService {



   

    @Value("${mpesa.api.consumer.key}")    
    private String consumerKey;

    @Value("${mpesa.api.consumer.secret}")
    private String consumerSecret;

     @Value("${mpesa.shortcode}")
    private String shortcode;

    @Value("${mpesa.passkey}")
    private String passkey;

    @Value("${mpesa.callbackurl}")
    private String callBackURL;


   

    


//Inital Authentication
    public String getAuthToken(){
         String url = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
         String credentials= consumerKey + ":" +consumerSecret;
         String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

          HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", "Basic " + encodedCredentials);

          RestTemplate restTemplate = new RestTemplate();
          HttpEntity<String> httpEntity = new HttpEntity<>(headers);
          ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);
          Map<String,Object> result = response.getBody();
          return (String)result.get("access_token");
    

 


    }



    public ResponseEntity<ApiResBaseClass> paymentRequest(PaymentRequestDto request) {
        try{

       String accessToken = getAuthToken();
       String formattedNumber = "254" + request.getPhoneNumber().substring(request.getPhoneNumber().length()-9);
       System.out.println("formatted number: " + formattedNumber);
       String url = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
       HttpHeaders headers = new HttpHeaders();
       String callBackUrlString = callBackURL+generateUniqueKey();
       String testCallBackUrl = "https://testfakeurll.com";
       String timestamp = generateTimeStamp();
       String password = generatePassword(timestamp);

        
       headers.add("Authorization","Bearer "+ accessToken);
       headers.setContentType(MediaType.APPLICATION_JSON);
       Map<String, Object> body = new HashMap<>();
       body.put("BusinessShortCode",shortcode);
       body.put("Password",password);
       body.put("Timestamp",timestamp);
       body.put("TransactionType", "CustomerPayBillOnline");
       body.put("Amount", request.getAmount());
       body.put("PartyA",formattedNumber);
       body.put("PartyB",shortcode);
       body.put("PhoneNumber", formattedNumber);
       body.put("CallBackURL",testCallBackUrl);
       body.put("AccountReference", "Test");
       body.put("TransactionDesc","Test");
       
      RestTemplate restTemplate = new RestTemplate();
      HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body,headers);
      ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
      Map<String,Object> result = response.getBody();
      if(result.isEmpty() || result == null){
         ErrorResponse errorResponse = new ErrorResponse();
          errorResponse.setMessage("Payment could not be processed.");
          errorResponse.setStatus("FAILED");
          errorResponse.setErrorType("PAYMENT_PROCESSING_ERROR");
          errorResponse.setErrorMessage("No response from API");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }

      StkPushResponse stkPushResponse = new StkPushResponse();
      stkPushResponse.setCheckoutRequestID(result.get( "CheckoutRequestID").toString());
      stkPushResponse.setCustomerMessage(result.get("CustomerMessage").toString());
      stkPushResponse.setMerchantRequestID(result.get("MerchantRequestID").toString());
      stkPushResponse.setResponseCode(result.get("ResponseCode").toString());
      stkPushResponse.setResponseDescription(result.get("ResponseDescription").toString());
      // replace with enum
      stkPushResponse.setStatus("SUCCESS");
      stkPushResponse.setMessage("A request has been sent to your phone, enter your pin to complete the payment");

      return ResponseEntity.ok( stkPushResponse);
       } catch (Exception e) {
          ErrorResponse errorResponse = new ErrorResponse();
          errorResponse.setMessage("Payment could not be processed");
          errorResponse.setStatus("FAILED");
          errorResponse.setErrorType("PAYMENT_PROCESSING_ERROR");
          errorResponse.setErrorMessage(e.getLocalizedMessage());

        return ResponseEntity.status(500).body(errorResponse);
        
       }


      

    }

    public ResponseEntity<ApiResBaseClass> transactionStatus(String checkOutRequestID){
        try{

        
      String authToken = getAuthToken();
      String url = "https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query";
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization","Bearer " + authToken);
      headers.setContentType(MediaType.APPLICATION_JSON);
      String timestamp = generateTimeStamp();
      String password = generatePassword(timestamp);
      Map<String,Object> body = new HashMap<>();
      
      body.put("Password", password);
      body.put("Timestamp", timestamp);
      body.put("BusinessShortCode", shortcode);
      body.put("CheckoutRequestID",checkOutRequestID);

      RestTemplate restTemplate = new RestTemplate();
      HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body,headers);
      ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
      Map<String,Object> result = response.getBody();
       if(result.isEmpty() || result.equals(null)){
         ErrorResponse errorResponse = new ErrorResponse();
          errorResponse.setMessage("Payment transaction status could not be processed.");
          errorResponse.setStatus("FAILED");
          errorResponse.setErrorType("PAYMENT_STATUS_PROCESSING_ERROR");
                    errorResponse.setErrorMessage("No response from API");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }

       StkPushResponse statusResponse = new StkPushResponse();
       statusResponse.setResponseCode(result.get("ResponseCode").toString());
       statusResponse.setResponseDescription(result.get("ResponseDescription").toString());
       statusResponse.setMerchantRequestID(result.get("MerchantRequestID").toString());
      statusResponse.setCheckoutRequestID(result.get("CheckoutRequestID").toString());
      statusResponse.setResultDesc(result.get( "ResultDesc").toString());
       // replace with enum
      statusResponse.setStatus("SUCCESS");
      statusResponse.setMessage("Your payment status has been sent successfully.");


         return ResponseEntity.ok( statusResponse);
        }catch(Exception e){
           ErrorResponse errorResponse = new ErrorResponse();
          errorResponse.setMessage("Payment status could not be processed.");
          errorResponse.setStatus("FAILED");
          errorResponse.setErrorType("PAYMENT_STATUS_PROCESSING_ERROR");
          errorResponse.setErrorMessage(e.getLocalizedMessage());

        return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private String generateTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }
    
    private String generatePassword(String timestamp){
       
        String password = shortcode + passkey + timestamp;
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
    private String generateUniqueKey(){
        return "";
    }
}