package com.teamx.mariaFoods.ui.fragments.Auth.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import com.teamx.mariaFoods.utils.UnAuthorizedCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _otpVerifyResponse = MutableLiveData<Resource<JsonObject>>()
    val otpVerifyResponse: LiveData<Resource<JsonObject>>
        get() = _otpVerifyResponse

    fun otpVerify(param : JsonObject,unAuthorizedCallback: UnAuthorizedCallback) {
        viewModelScope.launch {
            _otpVerifyResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.otpVerify(param) .let {
                        if (it.isSuccessful) {
                            _otpVerifyResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _otpVerifyResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                        } else {
                            _otpVerifyResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _otpVerifyResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _otpVerifyResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _otpVerifyForgotEmailResponse = MutableLiveData<Resource<JsonObject>>()
    val otpVerifyForgotEmailResponse: LiveData<Resource<JsonObject>>
        get() = _otpVerifyForgotEmailResponse

    fun otpVerifyForgotEmail(param : JsonObject,unAuthorizedCallback: UnAuthorizedCallback) {
        viewModelScope.launch {
            _otpVerifyForgotEmailResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.otpVerifForgotEmail(param) .let {
                        if (it.isSuccessful) {
                            _otpVerifyForgotEmailResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _otpVerifyForgotEmailResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                        } else {
                            _otpVerifyForgotEmailResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _otpVerifyForgotEmailResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _otpVerifyForgotEmailResponse.postValue(Resource.error("No internet connection", null))
        }
    }




//    private val _resendOtpResponse = MutableLiveData<Resource<SignupData>>()
//    val resendOtpResponse: LiveData<Resource<SignupData>>
//        get() = _resendOtpResponse
//
//    fun resendOtp(param : JsonObject,unAuthorizedCallback: UnAuthorizedCallback) {
//        viewModelScope.launch {
//            _resendOtpResponse.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                try {
//                    mainRepository.resendOtp(param) .let {
//                        if (it.isSuccessful) {
//                            _resendOtpResponse.postValue(Resource.success(it.body()!!))
//                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
//                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
//                            _resendOtpResponse.postValue(Resource.error(jsonObj.getString("message")))
//                        } else {
//                            _resendOtpResponse.postValue(Resource.error("Some thing went wrong", null))
//                        }
//                    }
//                } catch (e: Exception) {
//                    _resendOtpResponse.postValue(Resource.error("${e.message}", null))
//                }
//            } else _resendOtpResponse.postValue(Resource.error("No internet connection", null))
//        }
//    }


}