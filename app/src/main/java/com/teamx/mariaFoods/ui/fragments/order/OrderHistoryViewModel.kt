package com.teamx.mariaFoods.ui.fragments.order


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.orderHistory.OrderData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _orderListResponse = MutableLiveData<Resource<OrderData>>()
    val orderList: LiveData<Resource<OrderData>>
        get() = _orderListResponse
    fun getOrder() {
        viewModelScope.launch {
            _orderListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getOrder().let {
                        if (it.isSuccessful) {
                            _orderListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _orderListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _orderListResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _orderListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _orderListResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}