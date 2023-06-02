package com.teamx.mariaFoods.data.remote

import com.google.gson.JsonObject
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.mariaFoods.data.dataclasses.banners.BannerListData
import com.teamx.mariaFoods.data.dataclasses.login.LoginData
import com.teamx.mariaFoods.data.dataclasses.loginPhone.LoginPhoneData
import com.teamx.mariaFoods.data.dataclasses.orderHistory.OrderHistoryData
import com.teamx.mariaFoods.data.dataclasses.products.ProductsData
import com.teamx.mariaFoods.data.dataclasses.resetPass.ResetPassData
import com.teamx.mariaFoods.data.dataclasses.signup.SignupData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Get Post Update Delete
    @Headers("secret: dev")
    @POST(NetworkCallPoints.LOGIN)
    suspend fun loginEmail(@Body params: JsonObject?): Response<LoginData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.LOGIN_PHONE)
    suspend fun loginPhone(@Body params: JsonObject?): Response<LoginPhoneData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.SOCIAL_LOGIN)
    suspend fun socialLogins(@Body params: JsonObject?): Response<SignupData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.SIGN_UP)
    suspend fun signup(@Body params: JsonObject?): Response<SignupData>
    @Headers("secret: dev")
    @POST(NetworkCallPoints.LOGOUT)
    suspend fun logout(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>
    @Headers("secret: dev")
    @POST(NetworkCallPoints.RESET_PASS_EMAIL)
    suspend fun resetPassEmail(@Body params: JsonObject?): Response<ResetPassData>
 @Headers("secret: dev")
    @POST(NetworkCallPoints.RESET_PASS)
    suspend fun resetPass(@Body params: JsonObject?): Response<SuccessData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.OTP_VERIFY)
    suspend fun otpVerify(@Body params: JsonObject?): Response<SignupData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.OTP_VERIFY_FORGOT_EMAIL)
    suspend fun otpVerifForgotEmail(@Body params: JsonObject?): Response<SuccessData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.RESEND_OTP_VERIFY)
    suspend fun resendOtp(@Body params: JsonObject?): Response<SignupData>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.COUNTRIES)
    suspend fun getCountries(@Body params: JsonObject?): Response<SignupData>
    @Headers("secret: dev")
    @GET(NetworkCallPoints.BANNERS)
    suspend fun getBanners(): Response<BannerListData>
    @Headers("secret: dev")
    @GET(NetworkCallPoints.PRODUCTS)
    suspend fun getProducts(): Response<ProductsData>
 @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_ORDER_HISTORY)
    suspend fun getOrder(
     @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<OrderHistoryData>


}