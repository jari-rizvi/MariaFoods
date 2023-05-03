package com.teamx.mariaFoods.data.remote.reporitory

import com.google.gson.JsonObject
import com.teamx.mariaFoods.data.remote.ApiService
import retrofit2.http.Body
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService) {

    suspend fun loginPhone(@Body param: JsonObject) = apiService.loginPhone(param)
    suspend fun signup(@Body param: JsonObject) = apiService.signup(param)
    suspend fun otpVerify(@Body param: JsonObject) = apiService.otpVerify(param)
    suspend fun resendOtp(@Body param: JsonObject) = apiService.resendOtp(param)

}