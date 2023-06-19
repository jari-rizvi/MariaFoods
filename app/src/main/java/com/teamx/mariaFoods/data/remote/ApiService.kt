package com.teamx.mariaFoods.data.remote

import com.google.gson.JsonObject
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.mariaFoods.data.dataclasses.addtocart.AddToCartData
import com.teamx.mariaFoods.data.dataclasses.banners.BannerListData
import com.teamx.mariaFoods.data.dataclasses.checkout.CheckoutData
import com.teamx.mariaFoods.data.dataclasses.coupon.CouponData
import com.teamx.mariaFoods.data.dataclasses.editAddress.EditAddressData
import com.teamx.mariaFoods.data.dataclasses.getAddress.GetAddressData
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard.GetDefaultStripeCard
import com.teamx.mariaFoods.data.dataclasses.getStripecards.StripeCardsData
import com.teamx.mariaFoods.data.dataclasses.login.LoginData
import com.teamx.mariaFoods.data.dataclasses.loginPhone.LoginPhoneData
import com.teamx.mariaFoods.data.dataclasses.orderHistory.OrderData
import com.teamx.mariaFoods.data.dataclasses.products.ProductsData
import com.teamx.mariaFoods.data.dataclasses.resetPass.ResetPassData
import com.teamx.mariaFoods.data.dataclasses.signup.SignupData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.dataclasses.termsCondition.TermsConditonData
import com.teamx.mariaFoods.data.dataclasses.uploadProfile.UploadProfileData
import okhttp3.MultipartBody
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
    @POST(NetworkCallPoints.EDIT_PROFILE)
    suspend fun editProfile(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SignupData>


    @Headers("secret: dev")
    @Multipart
    @POST(NetworkCallPoints.PROFILE_IMAGE)
    suspend fun uploadProfile(
        @Part filePart: MultipartBody.Part,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<UploadProfileData>

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
    suspend fun resetPass(@Body params: JsonObject?): Response<LoginData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.CHANGE_PASSWORD)
    suspend fun changePass(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>
@Headers("secret: dev")
    @POST(NetworkCallPoints.CHANGE_PHONE)
    suspend fun changePhone(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<LoginPhoneData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.CHANGE_PHONE_VERIFY)
    suspend fun changePhoneVerify(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

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
    ): Response<OrderData>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_NOTIFICATION)
    suspend fun getNotification(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<JsonObject>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_ADDRESS)
    suspend fun getAddress(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<GetAddressData>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_CART)
    suspend fun getCart(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<GetCartData>
  @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_CARDS)
    suspend fun getCards(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<StripeCardsData>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_DEFAULT_CARDS)
    suspend fun getDefaultCard(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<GetDefaultStripeCard>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.CHECKOUT)
    suspend fun checkout(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<CheckoutData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.ADD_COUPON)
    suspend fun coupon(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<CouponData>


    //how to delete through retro
    @Headers("secret: dev")
    @HTTP(method = "DELETE", path = NetworkCallPoints.DELETE_ADDRESS)
    suspend fun deleteAddress(
        @Query("id") id: Int,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

    @Headers("secret: dev")
    @HTTP(method = "DELETE", path = NetworkCallPoints.DELETE_STRIPE_CARDS)
    suspend fun deleteCard(
        @Query("payment_method_id") id: String,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.ADD_ADDRESS)
    suspend fun addAddress(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.UPDATE_ADDRESS)
    suspend fun updateAddress(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

    @Headers("secret: dev")
    @GET(NetworkCallPoints.EDIT_ADDRESS)
    suspend fun editAddress(
        @Query("id") id: Int,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<EditAddressData>
  @Headers("secret: dev")
    @GET(NetworkCallPoints.TERMS_CONDITION)
    suspend fun termsCondition(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<TermsConditonData>
 @Headers("secret: dev")
    @GET(NetworkCallPoints.GET_HELP)
    suspend fun getHelp(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<TermsConditonData>

    @Headers("secret: dev")
    @POST(NetworkCallPoints.ADD_CART)
    suspend fun addCart(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<AddToCartData>


    @Headers("secret: dev")
    @POST(NetworkCallPoints.SET_DEFAULT_CARDS)
    suspend fun setDefaultCard(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

}