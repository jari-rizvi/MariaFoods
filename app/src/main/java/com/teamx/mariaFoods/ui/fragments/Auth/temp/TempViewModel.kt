package com.teamx.mariaFoods.ui.fragments.Auth.temp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TempViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _logoutResponse = MutableLiveData<Resource<SuccessData>>()
    val logoutResponse: LiveData<Resource<SuccessData>>
        get() = _logoutResponse
    fun logout() {
        viewModelScope.launch {
            _logoutResponse.postValue(Resource.loading(null))
            Log.d("TAG", "logoutPhone: first")
            Log.e("TAG", "logoutPhone: 1111111", )
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.logout().let {
                        if (it.isSuccessful) {
                            _logoutResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "logoutPhone: first1")
                            Log.e("TAG", "logoutPhone: 1111111", )

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            _logoutResponse.postValue(Resource.error(it.message(), null))
                            Log.d("TAG", "logoutPhone: first2")
                            Log.e("TAG", "logoutPhone: 1111111", )


                        } else {
                            _logoutResponse.postValue(Resource.error("Some thing went wrong", null))
                            Log.d("TAG", "logoutPhone: first3")
                            Log.e("TAG", "logoutPhone: 1111111", )

                        }
                    }
                } catch (e: Exception) {
                    _logoutResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _logoutResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}