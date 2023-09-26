package com.teamx.mariaFoods.data.remote.reporitory

import com.google.gson.JsonObject
import com.teamx.mariaFoods.data.remote.ApiService
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService) {

    suspend fun loginEmail(@Body param: JsonObject) = apiService.loginEmail(param)
    suspend fun loginPhone(@Body param: JsonObject) = apiService.loginPhone(param)
    suspend fun socialLogins(@Body param: JsonObject) = apiService.socialLogins(param)
    suspend fun editProfile(@Body param: JsonObject) = apiService.editProfile(param)
    suspend fun uploadProfile(param: MultipartBody.Part) = apiService.uploadProfile(param)
    suspend fun signup(@Body param: JsonObject) = apiService.signup(param)
    suspend fun logout() = apiService.logout()
    suspend fun resetPassEmail(@Body param: JsonObject) = apiService.resetPassEmail(param)
    suspend fun resetPass(@Body param: JsonObject) = apiService.resetPass(param)
    suspend fun changePass(@Body param: JsonObject) = apiService.changePass(param)
    suspend fun changePhone(@Body param: JsonObject) = apiService.changePhone(param)
    suspend fun changePhoneVerify(@Body param: JsonObject) = apiService.changePhoneVerify(param)
    suspend fun otpVerify(@Body param: JsonObject) = apiService.otpVerify(param)
    suspend fun otpVerifForgotEmail(@Body param: JsonObject) = apiService.otpVerifForgotEmail(param)
    suspend fun settingNotification(@Body param: JsonObject) = apiService.settingNotification(param)

    suspend fun getBanners() = apiService.getBanners()
    suspend fun getProducts() = apiService.getProducts()
    suspend fun getOrder() = apiService.getOrder()
    suspend fun getNotification() = apiService.getNotification()
    suspend fun getAddress() = apiService.getAddress()
    suspend fun getCart() = apiService.getCart()
    suspend fun getGuestCart(@Query("guest_id") guest_id: Int) = apiService.getGuestCart(guest_id)
    suspend fun removeCart(@Query("cart_id") cart_id: Int) = apiService.removeCart(cart_id)
    suspend fun getCards() = apiService.getCards()
    suspend fun getWishList() = apiService.getWishList()
    suspend fun getDefaultCard() = apiService.getDefaultCard()
    suspend fun checkout(@Body param: JsonObject) = apiService.checkout(param)
    suspend fun addWishList(@Body param: JsonObject) = apiService.addWishList(param)
    suspend fun cancelOrder(@Body param: JsonObject) = apiService.cancelOrder(param)
    suspend fun coupon(@Body param: JsonObject) = apiService.coupon(param)
    suspend fun deleteAddress(@Query("payment_method_id") id: Int) = apiService.deleteAddress(id)
    suspend fun deleteCard(@Query("id") id: String?) = apiService.deleteCard(id)
    suspend fun deleteWishlist(@Query("id") id: Int?) = apiService.deleteWishlist(id)
    suspend fun addAddress(@Body param: JsonObject) = apiService.addAddress(param)
    suspend fun changeSlot(@Body param: JsonObject) = apiService.changeSlot(param)
    suspend fun updateAddress(@Body param: JsonObject) = apiService.updateAddress(param)
    suspend fun editAddress(@Query("id") id: Int) = apiService.editAddress(id)
    suspend fun termsCondition() = apiService.termsCondition()
    suspend fun getHelp() = apiService.getHelp()
    suspend fun addCart(@Body param: JsonObject) = apiService.addCart(param)
    suspend fun setDefaultCard(@Body param: JsonObject) = apiService.setDefaultCard(param)



}