package com.teamx.mariaFoods.ui.fragments.Auth.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.login.LoginData
import com.teamx.mariaFoods.data.dataclasses.loginPhone.LoginPhoneData
import com.teamx.mariaFoods.data.dataclasses.signup.SignupData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _loginResponse = MutableLiveData<Resource<LoginData>>()
    val loginResponse: LiveData<Resource<LoginData>>
        get() = _loginResponse
    fun loginEmail(param: JsonObject) {
        viewModelScope.launch {
            _loginResponse.postValue(Resource.loading(null))
            Log.d("TAG", "loginPhone: first")
            Log.e("TAG", "loginPhone: 1111111", )
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.loginEmail(param).let {
                        if (it.isSuccessful) {
                            _loginResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "loginPhone: first1")
                            Log.e("TAG", "loginPhone: 1111111", )

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            _loginResponse.postValue(Resource.error(it.message(), null))
                            Log.d("TAG", "loginPhone: first2")
                            Log.e("TAG", "loginPhone: 1111111", )


                        } else {
                            _loginResponse.postValue(Resource.error("Some thing went wrong", null))
                            Log.d("TAG", "loginPhone: first3")
                            Log.e("TAG", "loginPhone: 1111111", )

                        }
                    }
                } catch (e: Exception) {
                    _loginResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _loginResponse.postValue(Resource.error("No internet connection", null))
        }
    }


  private val _loginPhoneResponse = MutableLiveData<Resource<LoginPhoneData>>()
    val loginPhoneResponse: LiveData<Resource<LoginPhoneData>>
        get() = _loginPhoneResponse
    fun loginPhone(param: JsonObject) {
        viewModelScope.launch {
            _loginPhoneResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.loginPhone(param).let {
                        if (it.isSuccessful) {
                            _loginPhoneResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _loginPhoneResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _loginPhoneResponse.postValue(Resource.error("Some thing went wrong", null))

                        }
                    }
                } catch (e: Exception) {
                    _loginPhoneResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _loginPhoneResponse.postValue(Resource.error("No internet connection", null))
        }
    }


 private val _socialLoginResponse = MutableLiveData<Resource<SignupData>>()
    val socialLoginResponse: LiveData<Resource<SignupData>>
        get() = _socialLoginResponse
    fun socialLogins(param: JsonObject) {
        viewModelScope.launch {
            _socialLoginResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.socialLogins(param).let {
                        if (it.isSuccessful) {
                            _socialLoginResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _socialLoginResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _socialLoginResponse.postValue(Resource.error("Some thing went wrong", null))

                        }
                    }
                } catch (e: Exception) {
                    _socialLoginResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _socialLoginResponse.postValue(Resource.error("No internet connection", null))
        }
    }






}