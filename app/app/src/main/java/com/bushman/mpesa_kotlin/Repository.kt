package com.bushman.mpesa_kotlin

import android.annotation.SuppressLint
import javax.inject.Inject
import retrofit2.HttpException


class Repository @Inject constructor(
   private val apiService: ApiService
) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun initializePayment(amount:String, phoneNumber:String): ApiResponse{

    val stkPushRequest = StkPushRequest(phoneNumber,amount)
    val stkPushResponse = apiService.sendStkPushRequest(stkPushRequest)

        if(!stkPushResponse.isSuccessful){
            throw HttpException(stkPushResponse)
        }
        val stkPushBody = stkPushResponse.body() ?: throw IllegalStateException("STK push response body is null")

        val requestCheckoutId = stkPushBody.checkoutRequestID ?: throw IllegalStateException("Checkout request ID is null")
        val paymentRes = apiService.checkTransactionStatus(requestId = requestCheckoutId)

        if(!paymentRes.isSuccessful){
            throw HttpException(paymentRes)
        }
        val paymentBody = paymentRes.body() ?: throw IllegalStateException("Transaction status body is null")

        return paymentBody

}}



