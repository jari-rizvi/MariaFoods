package com.teamx.mariaFoods.ui.fragments.Auth.changePassword


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.login.LoginData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _resetPasswordChangeResponse = MutableLiveData<Resource<LoginData>>()
    val resetPasswordChangeResponse: LiveData<Resource<LoginData>>
        get() = _resetPasswordChangeResponse

    fun resetPasswordChange(param: JsonObject) {
        viewModelScope.launch {

            _resetPasswordChangeResponse.postValue(Resource.loading(null))
            Log.d("TAG", "loginPhone: first")
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.resetPass(param).let {
                        if (it.isSuccessful) {
                            _resetPasswordChangeResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "loginPhone: sucess")
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _resetPasswordChangeResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                            Log.d("TAG", "loginPhone: ${it.code()}")
                        } else {
                            Log.d("TAG", "loginPhone: else")
                            _resetPasswordChangeResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _resetPasswordChangeResponse.postValue(Resource.error("${e.message}", null))
                }
            } else {
                _resetPasswordChangeResponse.postValue(Resource.error("No internet connection", null))
            }

        }
    }




}