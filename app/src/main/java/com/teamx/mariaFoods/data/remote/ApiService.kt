package com.teamx.mariaFoods.data.remote

import com.google.gson.JsonObject
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.login.LoginData
import com.teamx.mariaFoods.data.dataclasses.signup.SignupData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    //Get Post Update Delete
    @Headers("secret: dev")
    @POST(NetworkCallPoints.LOGIN)
    suspend fun loginPhone(@Body params: JsonObject?): Response<LoginData>

 @Headers("secret: dev")
    @POST(NetworkCallPoints.SIGN_UP)
    suspend fun signup(@Body params: JsonObject?): Response<SignupData>

    @POST(NetworkCallPoints.OTP_VERIFY)
    suspend fun otpVerify(@Body params: JsonObject?): Response<SignupData>


    @POST(NetworkCallPoints.RESEND_OTP_VERIFY)
    suspend fun resendOtp(@Body params: JsonObject?): Response<SignupData>


}