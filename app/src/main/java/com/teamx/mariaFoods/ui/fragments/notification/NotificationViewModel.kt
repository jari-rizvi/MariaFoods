package com.teamx.mariaFoods.ui.fragments.notification


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _notificationListResponse = MutableLiveData<Resource<JsonObject>>()
    val notificationList: LiveData<Resource<JsonObject>>
        get() = _notificationListResponse

    fun getnotification() {
        viewModelScope.launch {
            _notificationListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getNotification().let {
                        if (it.isSuccessful) {
                            _notificationListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _notificationListResponse.postValue(Resource.error(it.message(), null))

                        } else {
                            _notificationListResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _notificationListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _notificationListResponse.postValue(
                Resource.error(
                    "No internet connection", null
                )
            )
        }
    }

}