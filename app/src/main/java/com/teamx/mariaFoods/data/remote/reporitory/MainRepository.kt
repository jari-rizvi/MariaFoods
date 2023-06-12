package com.teamx.mariaFoods.data.remote.reporitory

import com.google.gson.JsonObject
import com.teamx.mariaFoods.data.remote.ApiService
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService) {

    suspend fun loginEmail(@Body param: JsonObject) = apiService.loginEmail(param)
    suspend fun loginPhone(@Body param: JsonObject) = apiService.loginPhone(param)
    suspend fun socialLogins(@Body param: JsonObject) = apiService.socialLogins(param)
    suspend fun editProfile(@Body param: JsonObject) = apiService.editProfile(param)
    suspend fun signup(@Body param: JsonObject) = apiService.signup(param)
    suspend fun logout() = apiService.logout()
    suspend fun resetPassEmail(@Body param: JsonObject) = apiService.resetPassEmail(param)
    suspend fun resetPass(@Body param: JsonObject) = apiService.resetPass(param)
    suspend fun changePass(@Body param: JsonObject) = apiService.changePass(param)
    suspend fun changePhone(@Body param: JsonObject) = apiService.changePhone(param)
    suspend fun changePhoneVerify(@Body param: JsonObject) = apiService.changePhoneVerify(param)
    suspend fun otpVerify(@Body param: JsonObject) = apiService.otpVerify(param)
    suspend fun otpVerifForgotEmail(@Body param: JsonObject) = apiService.otpVerifForgotEmail(param)

    suspend fun getBanners() = apiService.getBanners()
    suspend fun getProducts() = apiService.getProducts()
    suspend fun getOrder() = apiService.getOrder()
    suspend fun getNotification() = apiService.getNotification()
    suspend fun getAddress() = apiService.getAddress()
    suspend fun getCart() = apiService.getCart()
    suspend fun checkout(@Body param: JsonObject) = apiService.checkout(param)
    suspend fun coupon(@Body param: JsonObject) = apiService.coupon(param)
    suspend fun deleteAddress(@Query("id") id: Int) = apiService.deleteAddress(id)
    suspend fun addAddress(@Body param: JsonObject) = apiService.addAddress(param)
    suspend fun addCart(@Body param: JsonObject) = apiService.addCart(param)



}