package com.teamx.mariaFoods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val clickOnContinueBtn: MutableLiveData<Boolean>? = null



    val getCartList: LiveData<Resource<GetCartData>>
        get() = _getCartListResponse

    fun getCart() {
        viewModelScope.launch {
            _getCartListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCart().let {
                        if (it.isSuccessful) {
                            _getCartListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _getCartListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _getCartListResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCartListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getCartListResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}