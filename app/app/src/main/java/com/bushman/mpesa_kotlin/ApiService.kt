package com.bushman.mpesa_kotlin

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class StkPushRequest(
    val phoneNumber: String,
    val amount: String
)

data class TransactionStatusRequest(
    val requestId: String
)
data class ApiResponse(
    val error: String?,
    val message: String?,
    val merchantRequestID: String?,
    val checkoutRequestID: String?,
    val responseCode: String?,
    val responseDescription: String?,
    val customerMessage: String?,
    val resultDesc: String?,
    val errorType:String?,
    val errorMessage:String?
)
interface ApiService {
    @POST("/pay")
    suspend fun sendStkPushRequest(@Body request: StkPushRequest): Response<ApiResponse>

    @POST("/status")
    suspend fun checkTransactionStatus(@Body requestId:String):Response<ApiResponse>


}
