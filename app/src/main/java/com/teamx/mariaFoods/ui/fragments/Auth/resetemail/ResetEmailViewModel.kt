package com.teamx.mariaFoods.ui.fragments.Auth.resetemail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.resetPass.ResetPassData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ResetEmailViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _resetPassResponse = MutableLiveData<Resource<ResetPassData>>()
    val resetPassResponse: LiveData<Resource<ResetPassData>>
        get() = _resetPassResponse

    fun resetPass(param: JsonObject) {
        viewModelScope.launch {

            _resetPassResponse.postValue(Resource.loading(null))
            Log.d("TAG", "loginPhone: first")
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.resetPassEmail(param).let {
                        if (it.isSuccessful) {
                            _resetPassResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "loginPhone: sucess")
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _resetPassResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                            Log.d("TAG", "loginPhone: ${it.code()}")
                        } else {
                            Log.d("TAG", "loginPhone: else")
                            _resetPassResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _resetPassResponse.postValue(Resource.error("${e.message}", null))
                }
            } else {
                _resetPassResponse.postValue(Resource.error("No internet connection", null))
            }

        }
    }

}